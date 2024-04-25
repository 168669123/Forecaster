package wjh.projects.domain.transportVehicle.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 位置信息
 */
@Getter
@AllArgsConstructor
public class LocationVO implements Serializable {
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
}
