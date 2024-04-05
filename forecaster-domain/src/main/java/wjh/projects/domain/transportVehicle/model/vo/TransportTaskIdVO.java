package wjh.projects.domain.transportVehicle.model.vo;

import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
public class TransportTaskIdVO implements Identifier {
    private final String id;

    private TransportTaskIdVO() {
        id = null;
    }

    public TransportTaskIdVO(String id) {
        this.id = id;
    }
}
