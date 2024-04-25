package domain.estimateArrive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import wjh.projects.ForecasterApplication;
import wjh.projects.application.service.EstimateArriveAppService;
import wjh.projects.common.util.StringUtil;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.info.TransportTaskArriveInfo;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.estimateArrive.repository.EstimateArriveRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveClientTest {
    @Resource
    private EstimateArriveAppService estimateArriveAppService;
    @Resource
    private EstimateArriveRepository estimateArriveRepository;
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testSaveEstimateArrives() {
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
                new Date(),
                new Date()));

        EstimateArrive estimateArrive = new EstimateArrive(
                new EstimateArriveIdVO("testVehicle", "testId"),
                estimateArriveInfoVOs);

        estimateArriveRepository.save(estimateArrive);
        String key = StringUtil.append("truckNumber:", "testVehicle", ":msgId:", "testId");
        List<Object> objects = redisTemplate.opsForList().range(key, 0, -1);
        Assertions.assertTrue(objects != null && objects.size() == 1);
    }

    @Test
    void testClearEstimateArrives() {
        TransportTaskArriveInfo transportTaskArriveInfo = new TransportTaskArriveInfo();
        transportTaskArriveInfo.setTransportVehicleId("浙A9Z027");
        transportTaskArriveInfo.setTransportTaskId("C0A801AC00002A9F00045E38C4EC283B");
        estimateArriveAppService.clearEstimateArrives(transportTaskArriveInfo);

        String key = StringUtil.append("truckNumber:", "浙A9Z027", ":msgId:", "C0A801AC00002A9F00045E38C4EC283B");
        Set<String> keys = redisTemplate.keys(key);
        Assertions.assertTrue(keys == null || keys.size() == 0);
    }
}