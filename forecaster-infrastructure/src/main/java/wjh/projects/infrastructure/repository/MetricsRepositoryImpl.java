package wjh.projects.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import wjh.projects.common.exception.ResourceNotFoundException;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.vo.DataVO;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;
import wjh.projects.domain.metrics.repository.MetricsRepository;
import wjh.projects.infrastructure.converter.MetricsConverter;
import wjh.projects.infrastructure.converter.MetricsDOConverter;
import wjh.projects.infrastructure.dao.MetricsDO;
import wjh.projects.infrastructure.mapper.MetricsMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MetricsRepositoryImpl implements MetricsRepository {
    private final MetricsMapper metricsMapper;

    @Autowired
    public MetricsRepositoryImpl(MetricsMapper metricsMapper) {
        this.metricsMapper = metricsMapper;
    }

    @Override
    public void save(Metrics metrics) {
        MetricsDO metricsDO = MetricsConverter.toMetricsDO(metrics);
        metricsMapper.insert(metricsDO);
    }

    @Override
    public Metrics query(MetricsIdVO metricsIdVO) {
        return null;
    }

    @Override
    public void delete(MetricsIdVO metricsIdVO) {
    }

    @Override
    public List<Metrics> listMetrics(MetricsIdVO metricsIdVO) {
        List<Metrics> metricsList = new ArrayList<>();
        if (metricsIdVO.getDataKey() == null) {
            List<String> uniqueDataKeys = metricsMapper.listUniqueDataKeys(metricsIdVO);
            for (String uniqueDataKey : uniqueDataKeys) {
                MetricsIdVO idVO = new MetricsIdVO(
                        metricsIdVO.getId(),
                        metricsIdVO.getApplication(),
                        metricsIdVO.getGroup(),
                        uniqueDataKey);

                metricsList.add(new Metrics(idVO, new DataVO(null, null, null)));
            }
        } else {
            List<MetricsDO> metricsDOS = metricsMapper.listMetricsDO(metricsIdVO);
            for (MetricsDO metricsDO : metricsDOS)
                metricsList.add(MetricsDOConverter.toMetrics(metricsDO));
        }
        if (metricsList.size() == 0)
            throw new ResourceNotFoundException(JsonUtil.toJson(metricsIdVO) + "中存在错误的属性值");

        return metricsList;
    }
}
