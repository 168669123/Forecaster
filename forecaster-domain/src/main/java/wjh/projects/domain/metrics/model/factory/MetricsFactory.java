package wjh.projects.domain.metrics.model.factory;

import wjh.projects.common.IdGenerator;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;

public class MetricsFactory {

    public static Metrics create(String recordKey, int duration, int deviation) {
        MetricsIdVO id = new MetricsIdVO((int) IdGenerator.nextId(IdGenerator.METRICS_ESTIMATE));
        return new Metrics(id, recordKey, duration, deviation);
    }

    public static Metrics create(String recordKey, double latitude, double longitude) {
        MetricsIdVO id = new MetricsIdVO((int) IdGenerator.nextId(IdGenerator.METRICS_TRACK));
        return new Metrics(id, recordKey, latitude, longitude);
    }
}
