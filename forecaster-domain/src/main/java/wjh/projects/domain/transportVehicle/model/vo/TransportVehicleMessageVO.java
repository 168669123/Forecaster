package wjh.projects.domain.transportVehicle.model.vo;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * 运输车辆实时信息
 */
@Getter
public class TransportVehicleMessageVO implements Serializable {
    /**
     * 当前车辆位置
     */
    private LocationVO locationVO;

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

    public TransportVehicleMessageVO(LocationVO locationVO, Double speed, Date sendTime, Date createTime) {
        this.locationVO = locationVO;
        this.speed = speed;
        this.sendTime = sendTime;
        this.createTime = createTime;
    }
}
