package wjh.projects.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;
import wjh.projects.infrastructure.dao.MetricsDO;

import java.util.List;

@Mapper
public interface MetricsMapper {

    void insert(MetricsDO metricsDO);

    List<String> listUniqueDataKeys(MetricsIdVO metricsIdVO);

    List<MetricsDO> listMetricsDO(MetricsIdVO metricsIdVO);
}
