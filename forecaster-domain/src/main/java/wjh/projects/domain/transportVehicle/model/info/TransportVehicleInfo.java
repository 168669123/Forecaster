package wjh.projects.domain.transportVehicle.model.info;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransportVehicleInfo {
    private String carCode;
    private Double longitude;
    private Double latitude;
    private Date gpsTime;
    private Date createTime;
    private Double speed;
    private String address;
}
