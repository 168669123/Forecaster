package wjh.projects.domain.metrics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.factory.MetricsFactory;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

import java.util.List;
import java.util.Map;

/**
 * 数据记录领域服务
 */
@Service
public class MetricsDomainService {
    private static final Logger logger = LoggerFactory.getLogger(MetricsDomainService.class);

    /**
     * 监控运输车辆的预计到信息
     */
    public void monitoringEstimateArrive(TransportVehicle transportVehicle, List<EstimateArrive> estimateArrives) {
        if (transportVehicle == null)
            return;

        // 构建数据并执行记录
        for (EstimateArrive estimateArrive : estimateArrives) {
            Map<String, Integer> estimateDurations = estimateArrive.getEstimateDurations();
            logger.info("记录运输车辆：{}，预计到信息", transportVehicle.getId());
            for (Map.Entry<String, Integer> entry : estimateDurations.entrySet()) {
                String recordKey = transportVehicle.getId() + "_" + estimateArrive.getId() + "_" + entry.getKey();
                int deviation = transportVehicle.getTimeDeviation(estimateArrive.getId(), entry.getKey(), entry.getValue());
                Metrics metrics = MetricsFactory.create(recordKey, entry.getValue(), deviation);
                metrics.record();
            }
        }
    }

    /**
     * 跟踪运输车辆轨迹
     */
    public void trackTransportVehicle(TransportVehicle transportVehicle) {
        if (transportVehicle == null)
            return;

        // 构建数据并执行记录
        double[] geo = transportVehicle.getCurrentGeo();
        Metrics metrics = MetricsFactory.create(transportVehicle.getId(), geo[0], geo[1]);
        metrics.record();
        logger.info("记录运输车辆：{} 当前位置，纬度：{}、经度：{}", transportVehicle.getId(), geo[0], geo[1]);
    }
}
