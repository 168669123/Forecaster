package wjh.projects.domain.transportVehicle.model.vo;

import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
public class TransportTaskIdVO implements Identifier {
    private String transportTaskId;

    private TransportTaskIdVO() {
    }

    public TransportTaskIdVO(String transportTaskId) {
        this.transportTaskId = transportTaskId;
    }
}
