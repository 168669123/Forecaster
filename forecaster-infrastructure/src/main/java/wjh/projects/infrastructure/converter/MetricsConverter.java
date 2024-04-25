package wjh.projects.infrastructure.converter;

import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.infrastructure.dao.MetricsDO;

public class MetricsConverter {

    public static MetricsDO toMetricsDO(Metrics metrics) {
        MetricsDO metricsDO = new MetricsDO();

        metricsDO.setApplication(metrics.getMetricsIdVO().getApplication());
        metricsDO.setGroup(metrics.getMetricsIdVO().getGroup());
        metricsDO.setDataKey(metrics.getMetricsIdVO().getDataKey());
        metricsDO.setDataValue1(metrics.getDataVO().getDataValue1());
        metricsDO.setDataValue2(metrics.getDataVO().getDataValue2());
        metricsDO.setCreateTime(metrics.getDataVO().getCreateTime());

        return metricsDO;
    }
}
