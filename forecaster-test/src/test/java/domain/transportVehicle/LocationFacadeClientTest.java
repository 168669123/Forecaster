package domain.transportVehicle;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.rpc.LocationFacadeClient;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest(classes = ForecasterApplication.class)
class LocationFacadeClientTest {
    private static final Logger logger = LoggerFactory.getLogger(LocationFacadeClient.class);
    @Resource
    private LocationFacadeClient locationFacadeClient;

    @Test
    void testGeocode() {
        LocationVO geocode = locationFacadeClient.geocode("上海市浦东新区航头镇福善家园北176米");
        logger.info(JsonUtil.toJson(geocode));
    }

    @Test
    void testCalculate() {
        LocationVO currentLocation = new LocationVO("上海市浦东新区航头镇福善家园北176米");
        LinkedHashMap<Integer, LocationVO> unArrivedLocations = new LinkedHashMap<>();
        unArrivedLocations.put(1, new LocationVO("上海市松江区石湖荡镇龙地(上海)建设发展有限公司东273米"));
        unArrivedLocations.put(2, new LocationVO("浙江省金华市义乌市城西街道义乌市长堰水库管理处西南454米"));
        Map<Integer, Double> result = locationFacadeClient.calculate(currentLocation, unArrivedLocations);
        logger.info(JsonUtil.toJson(result));
    }
}