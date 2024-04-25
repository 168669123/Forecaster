package wjh.projects.domain.estimateArrive.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
@AllArgsConstructor
public class EstimateArriveIdVO implements Identifier {
    private String transportVehicleId;
    private String transportTaskId;

    private EstimateArriveIdVO() {
    }
}
