package infrasturcture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import wjh.projects.domain.transportVehicle.model.entity.Site;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;
import wjh.projects.infrastructure.rpc.impl.FacadeClientImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class FacadeClientImplTest {
    @InjectMocks
    private FacadeClientImpl facadeClient;

    @Test
    void testGeocode() {
        LocationVO geocode = facadeClient.geocode("上海市浦东新区航头镇福善家园北176米");
        Assertions.assertEquals(geocode.getLatitude(), 31.032835770374852);
        Assertions.assertEquals(geocode.getLongitude(), 121.62140475228213);
    }

    @Test
    void testCalculate() {
        LocationVO locationA = facadeClient.geocode("上海市浦东新区航头镇福善家园北176米");
        LocationVO locationB = facadeClient.geocode("上海市松江区石湖荡镇龙地(上海)建设发展有限公司东273米");
        List<Site> sites = new ArrayList<>();
        Site site = new Site(new SiteIdVO(null, locationB), null, null, null);
        sites.add(new Site(new SiteIdVO(null, locationB), null, null, null));
        Map<Site, Double> mileages = facadeClient.calculateMileages(locationA, sites);
        Assertions.assertEquals(mileages.get(site), 55538.0);
    }
}