package wjh.projects.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.info.TransportTaskArriveInfo;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.repository.EstimateArriveRepository;
import wjh.projects.domain.estimateArrive.service.EstimateArriveDomainService;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.factory.TransportVehicleFactory;
import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;

import java.util.List;

/**
 * 预计到应用服务
 */
@Service
public class EstimateArriveAppService {
    private final EstimateArriveDomainService estimateArriveDomainService;
    private final EstimateArriveRepository estimateArriveRepository;

    @Autowired
    public EstimateArriveAppService(EstimateArriveDomainService estimateArriveDomainService,
                                    EstimateArriveRepository estimateArriveRepository) {
        this.estimateArriveDomainService = estimateArriveDomainService;
        this.estimateArriveRepository = estimateArriveRepository;
    }

    /**
     * 计算预计到数据
     */
    public List<EstimateArrive> calculateEstimateArrive(TransportVehicleInfo transportVehicleInfo) {
        TransportVehicle transportVehicle = TransportVehicleFactory.create(transportVehicleInfo);
        List<EstimateArrive> estimateArrives = estimateArriveDomainService.calculate(transportVehicle);
        for (EstimateArrive estimateArrive : estimateArrives)
            estimateArriveRepository.save(estimateArrive);

        return estimateArrives;
    }

    /**
     * 清理预计到数据
     */
    public void clearEstimateArrives(TransportTaskArriveInfo transportTaskArriveInfo) {
        EstimateArriveIdVO estimateArriveIdVO = new EstimateArriveIdVO(
                transportTaskArriveInfo.getTransportVehicleId(),
                transportTaskArriveInfo.getTransportTaskId());

        estimateArriveRepository.delete(estimateArriveIdVO);
    }
}
