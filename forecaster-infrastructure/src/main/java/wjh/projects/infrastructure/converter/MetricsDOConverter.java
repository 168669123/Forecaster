package wjh.projects.infrastructure.converter;

import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.vo.DataVO;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;
import wjh.projects.infrastructure.dao.MetricsDO;

public class MetricsDOConverter {

    public static Metrics toMetrics(MetricsDO metricsDO) {
        MetricsIdVO metricsIdVO = new MetricsIdVO(
                metricsDO.getId(),
                metricsDO.getApplication(),
                metricsDO.getGroup(),
                metricsDO.getDataKey());

        DataVO dataVO = new DataVO(
                metricsDO.getDataValue1(),
                metricsDO.getDataValue2(),
                metricsDO.getCreateTime());

        return new Metrics(metricsIdVO, dataVO);
    }
}
