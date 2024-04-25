package wjh.projects.interfaces.converter;

import wjh.projects.domain.metrics.model.info.MetricsInfo;
import wjh.projects.facade.metrics.request.MetricsRequest;

public class MetricsRequestConverter {

    public static MetricsInfo toMetricsInfo(MetricsRequest metricsRequest) {
        MetricsInfo metricsInfo = new MetricsInfo();

        metricsInfo.setApplication(metricsRequest.getApplication());
        metricsInfo.setGroup(metricsRequest.getGroup());
        metricsInfo.setDataKey(metricsRequest.getDataKey());

        return metricsInfo;
    }
}
