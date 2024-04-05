package wjh.projects.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.repository.MetricsRepository;
import wjh.projects.infrastructure.converter.MetricsConverter;
import wjh.projects.infrastructure.dao.MetricsEstimateDO;
import wjh.projects.infrastructure.dao.MetricsTrackDO;
import wjh.projects.infrastructure.mapper.MetricsForecasterMapper;

@Repository
public class MetricsRepositoryImpl implements MetricsRepository {
    private final MetricsForecasterMapper metricsForecasterMapper;

    @Autowired
    public MetricsRepositoryImpl(MetricsForecasterMapper metricsForecasterMapper) {
        this.metricsForecasterMapper = metricsForecasterMapper;
    }

    @Override
    public void save(Metrics metrics) {
        if (metrics.getDuration() != null && metrics.getDeviation() != null) {
            MetricsEstimateDO metricsEstimateDO = MetricsConverter.toMetricsEstimateDO(metrics);
            metricsForecasterMapper.saveMetricsEstimateDO(metricsEstimateDO);
        } else if (metrics.getLatitude() != null && metrics.getLongitude() != null) {
            MetricsTrackDO metricsTrackDO = MetricsConverter.toMetricsTrackDO(metrics);
            metricsForecasterMapper.saveMetricsTrackDO(metricsTrackDO);
        }
    }
}
