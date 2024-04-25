package wjh.projects.facade.estimateArrive.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
public class EstimateRequest {
    private String rowKey;
    @NotNull(message = "运输车辆 id 不能为空")
    private String carCode;
    @NotNull(message = "运输车辆经度信息不能为空")
    private Double longitude;
    @NotNull(message = "运输车辆纬度信息不能为空")
    private Double latitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "运输车辆信息发送时间不能为空")
    private Date gpsTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "运输车辆信息创建时间不能为空")
    private Date createTime;
    private Double speed;
    @NotNull(message = "运输车辆地址不能为空")
    private String address;
    private Integer direction;
    private String dataType;
    private Double oldLongitude;
    private Double oldLatitude;
    private String acc;
    private String alarm;
    private Double distance;
    private Double oil;
    private String gpslock;
    private String hisflag;
    private String cretaeon;
}
