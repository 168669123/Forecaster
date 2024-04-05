package wjh.projects.domain.transportVehicle.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.common.util.SpringContextUtil;
import wjh.projects.domain.transportVehicle.rpc.LocationFacadeClient;

/**
 * 位置信息
 */
@Getter
@AllArgsConstructor
public class LocationVO {
    /**
     * 地址
     */
    private String address;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;

    private LocationVO() {
    }

    public LocationVO(String address) {
        LocationVO geocode = SpringContextUtil.getBean(LocationFacadeClient.class).geocode(address);
        this.address = geocode.address;
        this.latitude = geocode.latitude;
        this.longitude = geocode.longitude;
    }
}
