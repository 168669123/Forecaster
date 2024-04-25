package wjh.projects.interfaces.converter;

import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.facade.estimateArrive.response.EstimateArriveInfoDTO;

public class EstimateArriveInfoVOConverter {

    public static EstimateArriveInfoDTO toEstimateArriveInfoDTO(EstimateArriveInfoVO estimateArriveInfoVO) {
        EstimateArriveInfoDTO estimateArriveInfoDTO = new EstimateArriveInfoDTO();

        estimateArriveInfoDTO.setTruckNumber(estimateArriveInfoVO.getTruckNumber());
        estimateArriveInfoDTO.setAddress(estimateArriveInfoVO.getAddress());
        estimateArriveInfoDTO.setGpsTime(estimateArriveInfoVO.getGpsTime());
        estimateArriveInfoDTO.setBatchNumber(estimateArriveInfoVO.getBatchNumber());
        estimateArriveInfoDTO.setPreSiteId(estimateArriveInfoVO.getPreSiteId());
        estimateArriveInfoDTO.setArriveSiteId(estimateArriveInfoVO.getArriveSiteId());
        estimateArriveInfoDTO.setArriveCenterId(estimateArriveInfoVO.getArriveCenterId());
        estimateArriveInfoDTO.setSendSiteId(estimateArriveInfoVO.getSendSiteId());
        estimateArriveInfoDTO.setDistance(estimateArriveInfoVO.getDistance());
        estimateArriveInfoDTO.setEstimateArriveTime(estimateArriveInfoVO.getEstimateArriveTime());
        estimateArriveInfoDTO.setCalculateTime(estimateArriveInfoVO.getCalculateTime());

        return estimateArriveInfoDTO;
    }
}
