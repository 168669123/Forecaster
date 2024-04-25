package wjh.projects.facade.metrics;

import wjh.projects.facade.metrics.request.MetricsRequest;
import wjh.projects.facade.metrics.response.MetricsDTO;

import java.util.List;

/**
 * 数据记录门面接口
 */
public interface MetricsFacade {

    /**
     * 查询数据记录
     */
    List<MetricsDTO> listMetricsDTO(MetricsRequest metricsRequest);
}
