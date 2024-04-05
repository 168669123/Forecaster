package wjh.projects.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import wjh.projects.domain.estimateArrive.middleware.EstimateArriveClient;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.service.EstimateArriveDomainService;
import wjh.projects.domain.metrics.service.MetricsDomainService;
import wjh.projects.domain.transportVehicle.middleware.TransportVehicleClient;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Consumer;

/**
 * 预计到服务流程编排
 */
@Service
public class EstimateArriveService {
    private final TransportVehicleClient transportVehicleClient;
    private final EstimateArriveDomainService estimateArriveDomainService;
    private final MetricsDomainService metricsDomainService;
    private final EstimateArriveClient estimateArriveClient;

    @Autowired
    public EstimateArriveService(TransportVehicleClient transportVehicleClient,
                                 EstimateArriveDomainService estimateArriveDomainService,
                                 MetricsDomainService metricsDomainService,
                                 EstimateArriveClient estimateArriveClient) {
        this.transportVehicleClient = transportVehicleClient;
        this.estimateArriveDomainService = estimateArriveDomainService;
        this.metricsDomainService = metricsDomainService;
        this.estimateArriveClient = estimateArriveClient;
    }

    public void estimate() {
        Consumer<TransportVehicle> consumer = transportVehicle -> {
            // 跟踪运输车辆轨迹
            metricsDomainService.trackTransportVehicle(transportVehicle);
            // 计算运输车辆的预计到
            List<EstimateArrive> estimateArrives = estimateArriveDomainService.calculate(transportVehicle);
            // 监控预计到信息
            metricsDomainService.monitoringEstimateArrive(transportVehicle, estimateArrives);
            // 存储预计到信息
            estimateArriveClient.saveEstimateArrives(transportVehicle, estimateArrives);
        };

        // 消费运输车辆实时位置信息
        transportVehicleClient.consumeTransportVehicleInfo(consumer);
        // 清理已经到站的预计到信息
        estimateArriveClient.clearEstimateArrives();
    }
}
