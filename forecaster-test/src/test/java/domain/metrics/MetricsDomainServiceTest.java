package domain.metrics;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.domain.metrics.service.MetricsDomainService;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.factory.TransportVehicleFactory;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = ForecasterApplication.class)
class MetricsDomainServiceTest {
    @Resource
    private MetricsDomainService metricsDomainService;

    @Test
    void testTrackTransportVehicle() {
        TransportVehicle transportVehicle = TransportVehicleFactory.create(
                "浙A9Z027",
                "浙江省嘉兴市秀洲区王店镇雷家头北453米",
                30.664303,
                120.75632,
                30.0,
                new Date(),
                new Date());
        metricsDomainService.trackTransportVehicle(transportVehicle);
    }
}