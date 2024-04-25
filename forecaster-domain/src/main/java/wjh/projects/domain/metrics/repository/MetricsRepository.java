package wjh.projects.domain.metrics.repository;

import wjh.projects.domain.base.Repository;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;

import java.util.List;

public interface MetricsRepository extends Repository<Metrics, MetricsIdVO> {

    /**
     * 根据唯一标识查询数据记录集合
     */
    List<Metrics> listMetrics(MetricsIdVO metricsIdVO);
}
