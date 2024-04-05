package wjh.projects.infrastructure.middleware.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import wjh.projects.common.util.DateUtil;
import wjh.projects.common.util.FileUtil;
import wjh.projects.domain.transportVehicle.middleware.TransportVehicleClient;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.factory.TransportVehicleFactory;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
public class KafkaTransportVehicleClientImpl implements TransportVehicleClient {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTransportVehicleClientImpl.class);
    private Consumer<TransportVehicle> consumer;
    @Resource
    private KafkaListenerEndpointRegistry kafkaRegistry;
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void consumeTransportVehicleInfo(Consumer<TransportVehicle> consumer) {
        // 消费 Kafka 中运输车辆信息
//        kafkaRegistry.getListenerContainer("vehicleInfo").start();
//        this.consumer = consumer;

        // 为便于展示，此处暂时通过读取文件数据的方式代替从 kafka 中读取数据
        String[] filePaths = new String[]{
                "data/track_text_1709709469905.txt",
                "data/track_text_1709709496451.txt",
                "data/track_text_1709709526867.txt"};
        for (int i = 0; i < filePaths.length; i++) {
            URL resource = this.getClass().getClassLoader().getResource(filePaths[i]);
            try {
                filePaths[i] = URLDecoder.decode(Objects.requireNonNull(resource).getPath(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,
                5,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        for (String filePath : filePaths) {
            executor.execute(() -> {
                String jsonArray;
                while ((jsonArray = FileUtil.getInstance().getFixedSizeInTurnFromJsonArray(filePath, 1)) != null) {
                    logger.info("从文件：{}中消费运输车辆信息：{}", filePath, jsonArray);
                    JSONObject object = new JSONArray(jsonArray).getJSONObject(0);
                    consumer.accept(buildTransportVehicle(object));
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                logger.info("文件：{}数据读取完毕", filePath);
            });
        }
    }

    @KafkaListener(id = "vehicleInfo", topics = "???", groupId = "???", autoStartup = "false")
    public void consume(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        ConsumerRecord<?, ?> record,
                        Acknowledgment ack) {

        Optional<? extends ConsumerRecord<?, ?>> message = Optional.ofNullable(record);
        if (message.isPresent()) {
            JSONObject object = new JSONObject(message.get().value().toString());
            consumer.accept(buildTransportVehicle(object));
            ack.acknowledge();
        }
    }

    public TransportVehicle buildTransportVehicle(JSONObject object) {
        String carCode = object.getString("carCode");
        String address = object.getString("address");
        double latitude = object.getDouble("latitude");
        double longitude = object.getDouble("longitude");
        double speed = object.getDouble("speed");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date sendTime = DateUtil.stringToDate(object.getString("gpsTime"), pattern);
        Date createTime = DateUtil.stringToDate(object.getString("createTime"), pattern);

        return TransportVehicleFactory.create(carCode, address, latitude, longitude, speed, sendTime, createTime);
    }

    @Override
    public void sendTransportTaskArriveInfo(String info) {
        String topic = "transport_task_arrive";
        logger.info("准备发送车辆运输任务到车消息：{}", info);
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, info);

        // 监控消息发送情况
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                logger.info("向：{} 发送消息失败：{}", topic, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                logger.info("向：{} 发送消息成功：{}", topic, result.toString());
            }
        });
    }
}
