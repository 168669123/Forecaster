package domain.estimateArrive;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import wjh.projects.ForecasterApplication;
import wjh.projects.common.util.JsonUtil;
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

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveClientTest {
    private static final Logger logger = LoggerFactory.getLogger(EstimateArriveClientTest.class);
    @Mock
    private TransportVehicleMessageVO transportVehicleMessageVO;
    @Resource
    private EstimateArriveClient estimateArriveClient;
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void testListEstimateArrives() {
        List<EstimateArrive> estimateArrives = estimateArriveClient.listEstimateArrives("浙A9Z027");
        logger.info(JsonUtil.toJson(estimateArrives));
    }

    @Test
    void testSaveEstimateArrives() {
        TransportVehicleIdVO id = new TransportVehicleIdVO("浙A9Z027");
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
    }

    @Test
    void testClearEstimateArrives() {
        estimateArriveClient.clearEstimateArrives();
        kafkaTemplate.send("transport_task_arrive", "C0A801AC00002A9F00045E24B7122ECB");
        try {
            // 等待车辆运输任务到车信息的监听任务启动，并消费任务到车信息
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}