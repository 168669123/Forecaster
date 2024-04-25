package wjh.projects.interfaces.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wjh.projects.application.service.MetricsAppService;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.info.MetricsInfo;
import wjh.projects.facade.metrics.MetricsFacade;
import wjh.projects.facade.metrics.request.MetricsRequest;
import wjh.projects.facade.metrics.response.MetricsDTO;
import wjh.projects.interfaces.converter.MetricsConverter;
import wjh.projects.interfaces.converter.MetricsRequestConverter;

import java.util.ArrayList;
import java.util.List;

@Component
public class MetricsFacadeImpl implements MetricsFacade {
    private final MetricsAppService metricsAppService;

    @Autowired
    public MetricsFacadeImpl(MetricsAppService metricsAppService) {
        this.metricsAppService = metricsAppService;
    }

    @Override
    public List<MetricsDTO> listMetricsDTO(MetricsRequest metricsRequest) {
        MetricsInfo metricsInfo = MetricsRequestConverter.toMetricsInfo(metricsRequest);
        List<Metrics> metricsList = metricsAppService.listMetrics(metricsInfo);

        List<MetricsDTO> metricsDTOS = new ArrayList<>();
        for (Metrics metrics : metricsList)
            metricsDTOS.add(MetricsConverter.toMetricsDTO(metrics));

        return metricsDTOS;
    }
}
