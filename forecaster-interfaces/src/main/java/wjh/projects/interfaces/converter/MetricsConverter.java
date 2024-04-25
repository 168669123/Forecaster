package wjh.projects.interfaces.converter;

import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.facade.metrics.response.MetricsDTO;

public class MetricsConverter {

    public static MetricsDTO toMetricsDTO(Metrics metrics) {
        MetricsDTO metricsDTO = new MetricsDTO();

        metricsDTO.setDataKey(metrics.getMetricsIdVO().getDataKey());
        metricsDTO.setDataValue1(metrics.getDataVO().getDataValue1());
        metricsDTO.setDataValue2(metrics.getDataVO().getDataValue2());
        metricsDTO.setCreateTime(metrics.getDataVO().getCreateTime());

        return metricsDTO;
    }
}
