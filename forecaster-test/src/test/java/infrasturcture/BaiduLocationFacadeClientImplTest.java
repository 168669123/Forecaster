package infrasturcture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.infrastructure.rpc.BaiduLocationFacadeClientImpl;

import java.util.LinkedHashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class BaiduLocationFacadeClientImplTest {
    @InjectMocks
    private BaiduLocationFacadeClientImpl baiduLocationFacadeClient;

    @Test
    void testGeocode() {
        LocationVO geocode = baiduLocationFacadeClient.geocode("上海市浦东新区航头镇福善家园北176米");
        Assertions.assertEquals(geocode.getLatitude(), 31.032835770374852);
        Assertions.assertEquals(geocode.getLongitude(), 121.62140475228213);
    }

    @Test
    void testCalculate() {
        LocationVO locationA = baiduLocationFacadeClient.geocode("上海市浦东新区航头镇福善家园北176米");
        LocationVO locationB = baiduLocationFacadeClient.geocode("上海市松江区石湖荡镇龙地(上海)建设发展有限公司东273米");
        LinkedHashMap<Integer, LocationVO> unArrivedLocations = new LinkedHashMap<>();
        unArrivedLocations.put(1, locationB);
        Map<Integer, Double> result = baiduLocationFacadeClient.calculate(locationA, unArrivedLocations);
        Assertions.assertEquals(result.get(1), 55538.0);
    }
}