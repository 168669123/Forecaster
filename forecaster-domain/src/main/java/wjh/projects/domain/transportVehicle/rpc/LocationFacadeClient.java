package wjh.projects.domain.transportVehicle.rpc;

import wjh.projects.domain.transportVehicle.model.vo.LocationVO;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 位置信息外部 RPC 服务依赖接口
 */
public interface LocationFacadeClient {

    /**
     * 地理编码，将实际地址转换为含有经纬度的 LocationVO
     */
    LocationVO geocode(String address);

    /**
     * 计算从 currentLocation 到 unArrivedSites 各站点的里程，单位：m
     *
     * @return key：unArrivedSite 的 id，value：路线里程
     */
    Map<Integer, Double> calculate(LocationVO currentLocation, LinkedHashMap<Integer, LocationVO> unArrivedLocations);
}
