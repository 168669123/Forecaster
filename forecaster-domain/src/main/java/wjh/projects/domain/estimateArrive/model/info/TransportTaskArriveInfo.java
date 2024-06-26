package wjh.projects.domain.estimateArrive.model.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransportTaskArriveInfo {
    private String transportVehicleId;
    private String transportTaskId;
}