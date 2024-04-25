package wjh.projects.domain.estimateArrive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wjh.projects.common.IdGenerator;
import wjh.projects.common.constants.PropertiesEnum;
import wjh.projects.common.util.DateUtil;
import wjh.projects.common.util.PropertiesUtil;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.estimateArrive.repository.EstimateArriveRepository;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.entity.Site;
import wjh.projects.domain.transportVehicle.model.entity.TransportTask;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预计到领域服务
 */
@Service
public class EstimateArriveDomainService {
    private static final Logger logger = LoggerFactory.getLogger(EstimateArriveDomainService.class);
    private final EstimateArriveRepository estimateArriveRepository;

    @Autowired
    public EstimateArriveDomainService(EstimateArriveRepository estimateArriveRepository) {
        this.estimateArriveRepository = estimateArriveRepository;
    }

    /**
     * 计算运输车辆的预计到
     */
    public List<EstimateArrive> calculate(TransportVehicle transportVehicle) {
        List<EstimateArrive> newEstimateArrives = new ArrayList<>();
        if (transportVehicle == null)
            return newEstimateArrives;

        Long batchNumber = IdGenerator.nextId(IdGenerator.BATCH_NUMBER);
        for (TransportTask transportTask : transportVehicle.getTransportTasks()) {
            EstimateArriveIdVO estimateArriveIdVO = new EstimateArriveIdVO(
                    transportVehicle.getTransportVehicleIdVO().getTransportVehicleId(),
                    transportTask.getTransportTaskIdVO().getTransportTaskId());

            // 获取上次计算的运输车辆预计到
            EstimateArrive estimateArrive = estimateArriveRepository.query(estimateArriveIdVO);
            if (estimateArrive != null && estimateArrive.isUpToEstimateInterval()) {
                List<EstimateArriveInfoVO> estimateArriveInfoVOs = buildListEstimateArriveInfoVOs(
                        transportVehicle,
                        transportTask,
                        batchNumber);

                estimateArrive.setEstimateArriveInfoVOs(estimateArriveInfoVOs);
                newEstimateArrives.add(estimateArrive);
                logger.info("预计到计算完毕，运输车辆：{}、运输任务：{} ",
                        estimateArriveIdVO.getTransportVehicleId(),
                        estimateArriveIdVO.getTransportTaskId());
            }
        }
        return newEstimateArrives;
    }

    /**
     * 构建预计到信息集合
     *
     * @param transportVehicle 运输车辆
     * @param transportTask    车辆运输任务
     * @param batchNumber      预计到信息批次号
     */
    private List<EstimateArriveInfoVO> buildListEstimateArriveInfoVOs(TransportVehicle transportVehicle,
                                                                      TransportTask transportTask,
                                                                      Long batchNumber) {
        List<EstimateArriveInfoVO> estimateArriveInfoVOs = new ArrayList<>();
        Map<Site, Double> mileages = transportTask.getMileages();
        for (Map.Entry<Site, Double> entry : mileages.entrySet()) {
            SiteIdVO siteIdVO = entry.getKey().getSiteIdVO();
            int speed = Integer.parseInt(PropertiesUtil.get(PropertiesEnum.TRANSPORT_SPEED));
            double distance = entry.getValue();
            double duration = distance / (speed * 1000 / 3600.0);

            EstimateArriveInfoVO estimateArriveInfoVO = new EstimateArriveInfoVO(
                    transportVehicle.getTransportVehicleIdVO().getTransportVehicleId(),
                    transportTask.getDestinationAddress(),
                    transportVehicle.getTransportVehicleMessageVO().getSendTime(),
                    batchNumber,
                    transportTask.getPreSite(siteIdVO).getSiteIdVO().getSiteId(),
                    siteIdVO.getSiteId(),
                    null,
                    transportTask.getSendSite().getSiteIdVO().getSiteId(),
                    distance,
                    DateUtil.addSeconds(transportVehicle.getTransportVehicleMessageVO().getSendTime(), (int) duration),
                    new Date(),
                    transportTask.getPlanTime(siteIdVO));

            estimateArriveInfoVOs.add(estimateArriveInfoVO);
        }
        return estimateArriveInfoVOs;
    }
}
