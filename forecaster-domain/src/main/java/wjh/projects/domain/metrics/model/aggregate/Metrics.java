package wjh.projects.domain.metrics.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Setter;
import wjh.projects.common.util.DeepCopyUtil;
import wjh.projects.domain.base.AggregateRoot;
import wjh.projects.domain.metrics.model.vo.DataVO;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;

/**
 * 数据记录
 */
@Setter
@AllArgsConstructor
public class Metrics implements AggregateRoot<MetricsIdVO> {
    private MetricsIdVO metricsIdVO;
    private DataVO dataVO;

    private Metrics() {
    }

    public MetricsIdVO getMetricsIdVO() {
        return DeepCopyUtil.deepCopy(metricsIdVO, MetricsIdVO.class);
    }

    public DataVO getDataVO() {
        return DeepCopyUtil.deepCopy(dataVO, DataVO.class);
    }
}
