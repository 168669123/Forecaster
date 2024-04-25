package wjh.projects.interfaces.converter;

import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.facade.estimateArrive.response.EstimateArriveDTO;
import wjh.projects.facade.estimateArrive.response.EstimateArriveInfoDTO;

import java.util.ArrayList;
import java.util.List;

public class EstimateArriveConverter {

    public static EstimateArriveDTO toEstimateArriveDTO(EstimateArrive estimateArrive) {
        EstimateArriveDTO estimateArriveDTO = new EstimateArriveDTO();
        estimateArriveDTO.setMsgId(estimateArrive.getEstimateArriveIdVO().getTransportTaskId());

        List<EstimateArriveInfoDTO> estimateArriveInfoDTOS = new ArrayList<>();
        List<EstimateArriveInfoVO> estimateArriveInfoVOs = estimateArrive.getEstimateArriveInfoVOs();
        for (EstimateArriveInfoVO estimateArriveInfoVO : estimateArriveInfoVOs)
            estimateArriveInfoDTOS.add(EstimateArriveInfoVOConverter.toEstimateArriveInfoDTO(estimateArriveInfoVO));

        estimateArriveDTO.setEstimateArriveInfoDTOS(estimateArriveInfoDTOS);
        return estimateArriveDTO;
    }
}
