package wjh.projects.domain.metrics.repository;

import wjh.projects.domain.base.Repository;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;

public interface MetricsRepository extends Repository<Metrics, MetricsIdVO> {

    /**
     * 存储数据记录
     */
    void save(Metrics metrics);
}
