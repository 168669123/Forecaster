package wjh.projects.domain.metrics.model.aggregate;

import lombok.Getter;
import wjh.projects.common.util.SpringContextUtil;
import wjh.projects.domain.base.AggregateRoot;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;
import wjh.projects.domain.metrics.repository.MetricsRepository;

/**
 * 数据记录
 */
public class Metrics implements AggregateRoot<MetricsIdVO> {
    private MetricsIdVO metricsIdVO;
    @Getter
    private String recordKey;
    @Getter
    private Integer duration;
    @Getter
    private Integer deviation;
    @Getter
    private Double latitude;
    @Getter
    private Double longitude;

    private Metrics() {
    }

    public Metrics(MetricsIdVO metricsIdVO, String recordKey, Integer duration, Integer deviation) {
        this.metricsIdVO = metricsIdVO;
        this.recordKey = recordKey;
        this.duration = duration;
        this.deviation = deviation;
    }

    public Metrics(MetricsIdVO metricsIdVO, String recordKey, Double latitude, Double longitude) {
        this.metricsIdVO = metricsIdVO;
        this.recordKey = recordKey;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 存储数据记录
     */
    public void record() {
        SpringContextUtil.getBean(MetricsRepository.class).save(this);
    }

    public int getId() {
        return metricsIdVO.getId();
    }
}
