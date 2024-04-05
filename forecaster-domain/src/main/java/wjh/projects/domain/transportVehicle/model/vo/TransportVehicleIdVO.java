package wjh.projects.domain.transportVehicle.model.vo;

import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
public class TransportVehicleIdVO implements Identifier {
    private String id;

    private TransportVehicleIdVO() {
    }

    public TransportVehicleIdVO(String id) {
        this.id = id;
    }
}
