package wjh.projects.infrastructure.middleware.redis;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.common.util.StringUtil;
import wjh.projects.domain.estimateArrive.middleware.EstimateArriveClient;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * 以 redis 作为数据源
 */
@Service
public class RedisEstimateArriveClientImpl implements EstimateArriveClient {
    private static final Logger logger = LoggerFactory.getLogger(RedisEstimateArriveClientImpl.class);
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private KafkaListenerEndpointRegistry kafkaRegistry;
    private Function<String, Boolean> function;

    @Override
    public List<EstimateArrive> listEstimateArrives(String transportVehicleId) {
        Set<String> keys = redisTemplate.keys("truckNumber:" + transportVehicleId + ":msgId:*");
        List<EstimateArrive> estimateArrives = new ArrayList<>();
        if (keys == null)
            return estimateArrives;

        for (String key : keys) {
            List<Object> objects = redisTemplate.opsForList().range(key, 0, -1);
            if (objects == null)
                return estimateArrives;

            String estimateArriveId = key.substring(key.lastIndexOf(":") + 1);
            EstimateArriveIdVO estimateArriveIdVO = new EstimateArriveIdVO(estimateArriveId);
            List<EstimateArriveInfoVO> estimateArriveInfoVOs = new ArrayList<>();

            for (Object object : objects) {
                String jsonString = StringUtil.toJSONString(object.toString());
                estimateArriveInfoVOs.add(JsonUtil.parseJson(jsonString, EstimateArriveInfoVO.class));
            }
            estimateArrives.add(new EstimateArrive(estimateArriveIdVO, estimateArriveInfoVOs));
        }
        return estimateArrives;
    }

    @Override
    public void saveEstimateArrives(TransportVehicle transportVehicle, List<EstimateArrive> estimateArrives) {
        for (EstimateArrive estimateArrive : estimateArrives) {
            String key = "truckNumber:" + transportVehicle.getId() + ":msgId:" + estimateArrive.getId();
            logger.info("存储车辆运输任务：{}_{} 的预计到信息至 Redis", transportVehicle.getId(), estimateArrive.getId());
            redisTemplate.delete(key);
            for (String json : estimateArrive.getJsonOfEstimateArriveInfoVOs())
                redisTemplate.opsForList().rightPush(key, json);
        }
    }

    @Override
    public void clearEstimateArrives() {
        new Thread(() -> {
            // 等待 KafkaListener 方法注册到 KafkaListenerEndpointRegistry 中
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 定义预计到信息清理过程
            this.function = arriveInfo -> {
                Set<String> keys = redisTemplate.keys("*" + arriveInfo);
                if (keys == null || keys.size() == 0) {
                    return false;
                } else {
                    for (String key : keys)
                        redisTemplate.delete(key);
                }
                return true;
            };

            // 监听车辆运输任务到车信息
            MessageListenerContainer listener = kafkaRegistry.getListenerContainer("arriveInfo");
            if (listener == null) {
                logger.warn("KafkaListener：arriveInfo 还未注册到 KafkaListenerEndpointRegistry 中");
            } else {
                logger.info("KafkaListener：arriveInfo 启动，开始监听车辆运输任务到车信息");
                listener.start();
            }
        }).start();
    }

    @KafkaListener(id = "arriveInfo", topics = "transport_task_arrive", groupId = "forecaster", autoStartup = "false")
    public void clear(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      ConsumerRecord<?, ?> record,
                      Acknowledgment ack) {

        Optional<? extends ConsumerRecord<?, ?>> message = Optional.ofNullable(record);
        if (message.isPresent()) {
            String arriveInfo = message.get().value().toString();
            Boolean result = function.apply(arriveInfo);

            if (result) {
                logger.info("消费MQ消息，topic：{}，删除车辆运输任务：{} 的预计到信息", topic, arriveInfo);
            } else {
                logger.warn("消费MQ消息，topic：{}，目标车辆运输任务的预计到信息不存在：{}", topic, arriveInfo);
            }
            ack.acknowledge();
        }
    }
}
