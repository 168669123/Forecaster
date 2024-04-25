package wjh.projects.facade.estimateArrive.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EstimateArriveInfoDTO {
    /**
     * 运输车辆 id
     */
    private String truckNumber;
    /**
     * 目的地地址
     */
    private String address;
    /**
     * GPS 信息发送时间日期
     */
    private Date gpsTime;
    /**
     * 批次号
     */
    private Long batchNumber;
    /**
     * 上一个站点 id
     */
    private Integer preSiteId;
    /**
     * 目标站点 id
     */
    private Integer arriveSiteId;
    /**
     * 目标转运中心 id
     */
    private Integer arriveCenterId;
    /**
     * 发车站点 id
     */
    private Integer sendSiteId;
    /**
     * 当前位置到目标站点的距离
     */
    private Double distance;
    /**
     * 预计到时间
     */
    private Date estimateArriveTime;
    /**
     * 开始计算预计到时间的时间
     */
    private Date calculateTime;
}
