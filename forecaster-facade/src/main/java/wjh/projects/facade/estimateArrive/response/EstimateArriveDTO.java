package wjh.projects.facade.estimateArrive.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EstimateArriveDTO {
    private String truckNumber;
    private String msgId;
    private List<EstimateArriveInfoDTO> estimateArriveInfoDTOS;
}
