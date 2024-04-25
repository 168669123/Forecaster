package domain.estimateArrive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.service.EstimateArriveDomainService;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.factory.TransportVehicleFactory;
import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveDomainServiceTest {
    @Resource
    private EstimateArriveDomainService estimateArriveDomainService;

    @Test
    void testCalculate() {
        TransportVehicleInfo transportVehicleInfo = new TransportVehicleInfo();
        transportVehicleInfo.setCarCode("浙A9Z027");
        transportVehicleInfo.setLongitude(120.75632);
        transportVehicleInfo.setLatitude(30.664303);
        transportVehicleInfo.setGpsTime(new Date());
        transportVehicleInfo.setCreateTime(new Date());
        transportVehicleInfo.setSpeed(30.0);
        transportVehicleInfo.setAddress("浙江省嘉兴市秀洲区王店镇雷家头北453米");

        TransportVehicle transportVehicle = TransportVehicleFactory.create(transportVehicleInfo);
        List<EstimateArrive> estimateArrives = estimateArriveDomainService.calculate(transportVehicle);
        Assertions.assertNotNull(estimateArrives);
    }
}