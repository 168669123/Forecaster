package wjh.projects.domain.transportVehicle.model.vo;

import lombok.Getter;

import java.util.Date;

/**
 * 运输车辆实时信息
 */
@Getter
public class TransportVehicleMessageVO {
    /**
     * 当前车辆位置
     */
    private LocationVO location;

    /**
     * 当前行驶速度，单位 km/h
     */
    private Double speed;
    /**
     * 信息发送时间
     */
    private Date sendTime;
    /**
     * 信息接收时间
     */
    private Date createTime;

    private TransportVehicleMessageVO() {
    }

    public TransportVehicleMessageVO(LocationVO location, Double speed, Date sendTime, Date createTime) {
        this.location = location;
        this.speed = speed;
        this.sendTime = sendTime;
        this.createTime = createTime;
    }
}
