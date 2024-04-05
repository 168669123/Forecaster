package wjh.projects.domain.estimateArrive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wjh.projects.common.Environment;
import wjh.projects.common.IdGenerator;
import wjh.projects.common.util.DateUtil;
import wjh.projects.domain.estimateArrive.middleware.EstimateArriveClient;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

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
    private final EstimateArriveClient estimateArriveClient;

    @Autowired
    public EstimateArriveDomainService(EstimateArriveClient estimateArriveClient) {
        this.estimateArriveClient = estimateArriveClient;
    }

    /**
     * 计算运输车辆的预计到
     */
    public List<EstimateArrive> calculate(TransportVehicle transportVehicle) {
        List<EstimateArrive> newEstimateArrives = new ArrayList<>();
        if (transportVehicle == null)
            return newEstimateArrives;

        // 获取运输车辆上次计算的预计到
        List<EstimateArrive> oldEstimateArrives = estimateArriveClient.listEstimateArrives(transportVehicle.getId());
        List<String> transportTaskIds = transportVehicle.getTransportTaskIds();
        Long batchNumber = null;

        // 依次计算车辆运输任务的预计到
        for (String transportTaskId : transportTaskIds) {
            if (validate(transportTaskId, oldEstimateArrives)) {
                batchNumber = batchNumber == null ? IdGenerator.nextId(IdGenerator.BATCH_NUMBER) : batchNumber;
                List<EstimateArriveInfoVO> estimateArriveInfoVOs = buildListEstimateArriveInfoVOs(
                        transportVehicle,
                        transportTaskId,
                        transportVehicle.calculateDistanceMap(transportTaskId),
                        batchNumber);

                EstimateArriveIdVO id = new EstimateArriveIdVO(transportTaskId);
                newEstimateArrives.add(new EstimateArrive(id, estimateArriveInfoVOs));
                logger.info("车辆运输任务：{} 预计到计算完成", transportTaskId);
            }
        }

        return newEstimateArrives;
    }

    /**
     * 判断当前车辆运输任务是否满足预计到的计算条件
     *
     * @return 当预计到存在，并且计算间隔未达到预设值时返回 false，否则返回 true
     */
    private boolean validate(String transportTaskId, List<EstimateArrive> oldEstimateArrives) {
        for (EstimateArrive oldEstimateArrive : oldEstimateArrives) {
            // 判断当前车辆运输任务和预计到是否匹配
            if (oldEstimateArrive.getId().equals(transportTaskId)) {
                // 判断预计到计算间隔是否达到预设值
                if (!oldEstimateArrive.isUpToEstimateInterval())
                    return false;
            }
        }
        return true;
    }

    /**
     * 构建预计到信息集合
     *
     * @param transportVehicle 运输车辆
     * @param transportTaskId  车辆运输任务 id
     * @param distanceMap      key：unArrivedSite 的 id，value：路线里程，单位：m
     * @param batchNumber      预计到信息批次号
     */
    private List<EstimateArriveInfoVO> buildListEstimateArriveInfoVOs(TransportVehicle transportVehicle,
                                                                      String transportTaskId,
                                                                      Map<Integer, Double> distanceMap,
                                                                      Long batchNumber) {

        List<EstimateArriveInfoVO> estimateArriveInfoVOs = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : distanceMap.entrySet()) {
            int siteId = entry.getKey();
            double distance = entry.getValue();
            double duration = distance / (Environment.getTransportSpeed() * 1000 / 3600.0);

            EstimateArriveInfoVO estimateArriveInfoVO = new EstimateArriveInfoVO(
                    transportVehicle.getId(),
                    transportVehicle.getDestinationAddress(transportTaskId),
                    transportVehicle.getMessageSendTime(),
                    batchNumber,
                    transportVehicle.getPreSiteId(transportTaskId, siteId),
                    siteId,
                    null,
                    transportVehicle.getSendSiteId(transportTaskId),
                    distance,
                    DateUtil.addSeconds(transportVehicle.getMessageSendTime(), (int) duration),
                    new Date());

            estimateArriveInfoVOs.add(estimateArriveInfoVO);
        }
        return estimateArriveInfoVOs;
    }
}
