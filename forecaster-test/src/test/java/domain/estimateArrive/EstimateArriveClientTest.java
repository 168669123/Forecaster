package domain.estimateArrive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import wjh.projects.ForecasterApplication;
import wjh.projects.domain.estimateArrive.middleware.EstimateArriveClient;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveClientTest {
    @Mock
    private TransportVehicleMessageVO transportVehicleMessageVO;
    @Resource
    private EstimateArriveClient estimateArriveClient;
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testListEstimateArrives() {
        List<EstimateArrive> estimateArrives = estimateArriveClient.listEstimateArrives("浙A9Z027");
        Assertions.assertNotNull(estimateArrives);
    }

    @Test
    void testSaveEstimateArrives() {
        TransportVehicleIdVO id = new TransportVehicleIdVO("testVehicle");
        TransportVehicle transportVehicle = new TransportVehicle(id, transportVehicleMessageVO);

        List<EstimateArrive> estimateArrives = new ArrayList<>();
        List<EstimateArriveInfoVO> estimateArriveInfoVOs = new ArrayList<>();
        estimateArriveInfoVOs.add(new EstimateArriveInfoVO(
                "1",
                "1",
                new Date(),
                1L,
                1,
                1,
                1,
                1,
                1.0,
                new Date(),
                new Date()));
        estimateArrives.add(new EstimateArrive(new EstimateArriveIdVO("testId"), estimateArriveInfoVOs));
        estimateArriveClient.saveEstimateArrives(transportVehicle, estimateArrives);
        Set<String> keys = redisTemplate.keys("truckNumber:" + transportVehicle.getId() + ":msgId:testId");
        Assertions.assertTrue(keys != null && keys.size() == 1);
    }

    @Test
    void testClearEstimateArrives() {
        String arriveInfo = "C0A801AC00002A9F00045E24B7122ECB";
        estimateArriveClient.clearEstimateArrives();
        kafkaTemplate.send("transport_task_arrive", arriveInfo);
        try {
            // 等待车辆运输任务到车信息的监听任务启动，并消费任务到车信息
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Set<String> keys = redisTemplate.keys("*" + arriveInfo);
        Assertions.assertTrue(keys == null || keys.size() == 0);
    }
}