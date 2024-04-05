package wjh.projects.infrastructure.converter;

import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.infrastructure.dao.MetricsEstimateDO;
import wjh.projects.infrastructure.dao.MetricsTrackDO;

import java.util.Date;

public class MetricsConverter {

    public static MetricsEstimateDO toMetricsEstimateDO(Metrics metrics) {
        MetricsEstimateDO metricsEstimateDO = new MetricsEstimateDO();

        metricsEstimateDO.setRecordKey(metrics.getRecordKey());
        metricsEstimateDO.setDuration(metrics.getDuration());
        metricsEstimateDO.setDeviation(metrics.getDeviation());
        metricsEstimateDO.setCreateTime(new Date());

        return metricsEstimateDO;
    }

    public static MetricsTrackDO toMetricsTrackDO(Metrics metrics) {
        MetricsTrackDO metricsTrackDO = new MetricsTrackDO();

        metricsTrackDO.setRecordKey(metrics.getRecordKey());
        metricsTrackDO.setLatitude(metrics.getLatitude());
        metricsTrackDO.setLongitude(metrics.getLongitude());
        metricsTrackDO.setCreateTime(new Date());

        return metricsTrackDO;
    }
}
