package wjh.projects.domain.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.repository.MetricsRepository;

/**
 * 数据记录领域服务
 */
@Service
public class MetricsDomainService {
    private final MetricsRepository metricsRepository;

    @Autowired
    public MetricsDomainService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    /**
     * 进行数据打点
     */
    public void dot(Metrics metrics) {
        metricsRepository.save(metrics);
    }
}
