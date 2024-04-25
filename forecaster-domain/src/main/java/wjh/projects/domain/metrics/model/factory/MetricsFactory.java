package wjh.projects.domain.metrics.model.factory;

import wjh.projects.common.constants.PropertiesEnum;
import wjh.projects.common.util.PropertiesUtil;
import wjh.projects.domain.metrics.model.aggregate.Metrics;
import wjh.projects.domain.metrics.model.vo.DataVO;
import wjh.projects.domain.metrics.model.vo.MetricsIdVO;

import java.util.Date;

public class MetricsFactory {

    public static Metrics create(String group, String dataKey, Double dataValue1, Double dataValue2) {
        String application = PropertiesUtil.get(PropertiesEnum.METRICS_APPLICATION);
        MetricsIdVO metricsIdVO = new MetricsIdVO(null, application, group, dataKey);
        DataVO dataVO = new DataVO(dataValue1, dataValue2, new Date());
        return new Metrics(metricsIdVO, dataVO);
    }
}
