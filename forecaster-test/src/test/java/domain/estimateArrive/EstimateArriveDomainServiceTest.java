package domain.estimateArrive;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.service.EstimateArriveDomainService;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.factory.TransportVehicleFactory;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = ForecasterApplication.class)
class EstimateArriveDomainServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(EstimateArriveDomainServiceTest.class);
    @Resource
    private EstimateArriveDomainService estimateArriveDomainService;

    @Test
    void testCalculate() {
        TransportVehicle transportVehicle = TransportVehicleFactory.create(
                "浙A9Z027",
                "浙江省嘉兴市秀洲区王店镇雷家头北453米",
                30.664303,
                120.75632,
                30.0,
                new Date(),
                new Date());
        List<EstimateArrive> estimateArrives = estimateArriveDomainService.calculate(transportVehicle);
        logger.info(JsonUtil.toJson(estimateArrives));
    }
}