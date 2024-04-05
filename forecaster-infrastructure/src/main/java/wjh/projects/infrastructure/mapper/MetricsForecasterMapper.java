package wjh.projects.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import wjh.projects.infrastructure.dao.MetricsEstimateDO;
import wjh.projects.infrastructure.dao.MetricsTrackDO;

@Mapper
public interface MetricsForecasterMapper {

    void saveMetricsEstimateDO(MetricsEstimateDO metricsEstimateDO);

    void saveMetricsTrackDO(MetricsTrackDO metricsTrackDO);
}
