package wjh.projects.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wjh.projects.common.util.StringUtil;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.factory.MetricsFactory;
import wjh.projects.domain.metrics.model.info.MetricsInfo;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;
import wjh.projects.domain.metrics.repository.MetricsRepository;
import wjh.projects.domain.metrics.service.MetricsDomainService;
import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;

import java.util.Date;
import java.util.List;

/**
 * 数据记录应用服务
 */
@Service
public class MetricsAppService {
    private static final Logger logger = LoggerFactory.getLogger(MetricsAppService.class);
    private final MetricsDomainService metricsDomainService;
    private final MetricsRepository metricsRepository;

    @Autowired
    public MetricsAppService(MetricsDomainService metricsDomainService, MetricsRepository metricsRepository) {
        this.metricsDomainService = metricsDomainService;
        this.metricsRepository = metricsRepository;
    }

    /**
     * 跟踪运输车辆轨迹
     */
    public void trackTransportVehicle(TransportVehicleInfo transportVehicleInfo) {
        Metrics metrics = MetricsFactory.create(
                "trackTransportVehicle",
                transportVehicleInfo.getCarCode(),
                transportVehicleInfo.getLatitude(),
                transportVehicleInfo.getLongitude());

        metricsDomainService.dot(metrics);
        logger.info("记录运输车辆{}的当前位置：纬度：{}，经度：{}",
                transportVehicleInfo.getCarCode(),
                transportVehicleInfo.getLatitude(),
                transportVehicleInfo.getLongitude());
    }

    /**
     * 监控预计到信息
     */
    public void monitorEstimateArrive(TransportVehicleInfo transportVehicleInfo, List<EstimateArrive> estimateArrives) {
        for (EstimateArrive estimateArrive : estimateArrives) {
            for (EstimateArriveInfoVO estimateArriveInfoVO : estimateArrive.getEstimateArriveInfoVOs()) {
                String dataKey = StringUtil.append(transportVehicleInfo.getCarCode(), "_",
                        estimateArrive.getEstimateArriveIdVO().getTransportTaskId(), "_",
                        estimateArriveInfoVO.getAddress());

                Date sendTime = transportVehicleInfo.getGpsTime();
                Date planTime = estimateArriveInfoVO.getPlanTime();
                Date estimateArriveTime = estimateArriveInfoVO.getEstimateArriveTime();
                Double duration = (estimateArriveTime.getTime() - sendTime.getTime()) / 1000.0;
                Double deviation = (estimateArriveTime.getTime() - planTime.getTime()) / 1000.0;

                Metrics metrics = MetricsFactory.create(
                        "monitorEstimateArrive",
                        dataKey,
                        duration,
                        deviation);

                metricsDomainService.dot(metrics);
            }
        }
        logger.info("记录运输车辆{}的预计到信息", transportVehicleInfo.getCarCode());
    }

    /**
     * 查询数据记录
     */
    public List<Metrics> listMetrics(MetricsInfo metricsInfo) {
        MetricsIdVO metricsIdVO = new MetricsIdVO(
                null,
                metricsInfo.getApplication(),
                metricsInfo.getGroup(),
                metricsInfo.getDataKey());

        return metricsRepository.listMetrics(metricsIdVO);
    }
}
