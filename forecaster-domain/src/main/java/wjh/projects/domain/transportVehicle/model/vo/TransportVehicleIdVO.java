package wjh.projects.domain.transportVehicle.model.vo;

import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
public class TransportVehicleIdVO implements Identifier {
    private String transportVehicleId;

    private TransportVehicleIdVO() {
    }

    public TransportVehicleIdVO(String transportVehicleId) {
        this.transportVehicleId = transportVehicleId;
    }
}
