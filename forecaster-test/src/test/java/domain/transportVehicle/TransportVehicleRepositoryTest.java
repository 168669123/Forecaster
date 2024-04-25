package domain.transportVehicle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;

import javax.annotation.Resource;

@SpringBootTest(classes = ForecasterApplication.class)
class TransportVehicleRepositoryTest {
    @Resource
    private TransportVehicleRepository transportVehicleRepository;

    @Test
    void testAssignTransportTasks() {
        TransportVehicleIdVO id = new TransportVehicleIdVO("浙A9Z027");
        TransportVehicle transportVehicle = new TransportVehicle(id, null, null);
        transportVehicleRepository.assignTransportTasks(transportVehicle);
        Assertions.assertNotNull(transportVehicle.getTransportTasks());
    }
}