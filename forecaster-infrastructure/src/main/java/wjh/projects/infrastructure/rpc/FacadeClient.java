package wjh.projects.infrastructure.rpc;

import wjh.projects.domain.transportVehicle.model.entity.Site;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;

import java.util.List;
import java.util.Map;

/**
 * 外部 RPC 服务依赖接口
 */
public interface FacadeClient {

    /**
     * 地理编码，将实际地址转换为经纬度信息
     */
    LocationVO geocode(String address);

    /**
     * 批量算路，计算当前位置到各个未到车运输站点的里程，单位：m
     */
    Map<Site, Double> calculateMileages(LocationVO current, List<Site> sites);
}
