package domain.transportVehicle;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;

import javax.annotation.Resource;

@SpringBootTest(classes = ForecasterApplication.class)
class TransportVehicleRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(TransportVehicleRepositoryTest.class);
    @Mock
    private TransportVehicleMessageVO transportVehicleMessageVO;
    @Resource
    private TransportVehicleRepository transportVehicleRepository;

    @Test
    void testAssignTransportTasks() {
        TransportVehicleIdVO id = new TransportVehicleIdVO("浙A9Z027");
        TransportVehicle transportVehicle = new TransportVehicle(id, transportVehicleMessageVO);
        transportVehicleRepository.assignTransportTasks(transportVehicle);
        logger.info("运输任务id：{}", transportVehicle.getTransportTaskIds());
    }
}