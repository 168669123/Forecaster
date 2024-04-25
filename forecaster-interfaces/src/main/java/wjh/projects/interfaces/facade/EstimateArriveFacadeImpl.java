package wjh.projects.interfaces.facade;

import org.springframework.stereotype.Component;
import wjh.projects.application.service.EstimateArriveAppService;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;
import wjh.projects.facade.estimateArrive.EstimateArriveFacade;
import wjh.projects.facade.estimateArrive.response.EstimateArriveDTO;
import wjh.projects.facade.estimateArrive.request.EstimateRequest;
import wjh.projects.interfaces.converter.EstimateArriveConverter;
import wjh.projects.interfaces.converter.EstimateRequestConverter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class EstimateArriveFacadeImpl implements EstimateArriveFacade {
    @Resource
    private EstimateArriveAppService estimateArriveAppService;

    @Override
    public List<EstimateArriveDTO> estimate(EstimateRequest estimateRequest) {
        TransportVehicleInfo transportVehicleInfo = EstimateRequestConverter.toTransportVehicleInfo(estimateRequest);
        List<EstimateArrive> estimateArrives = estimateArriveAppService.calculateEstimateArrive(transportVehicleInfo);

        List<EstimateArriveDTO> estimateArriveDTOS = new ArrayList<>();
        for (EstimateArrive estimateArrive : estimateArrives) {
            EstimateArriveDTO estimateArriveDTO = EstimateArriveConverter.toEstimateArriveDTO(estimateArrive);
            estimateArriveDTO.setTruckNumber(transportVehicleInfo.getCarCode());
            estimateArriveDTOS.add(estimateArriveDTO);
        }
        return estimateArriveDTOS;
    }
}
