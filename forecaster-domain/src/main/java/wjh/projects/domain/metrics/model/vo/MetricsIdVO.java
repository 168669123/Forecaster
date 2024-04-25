package wjh.projects.domain.metrics.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
@AllArgsConstructor
public class MetricsIdVO implements Identifier {
    private Integer id;
    private String application;
    private String group;
    private String dataKey;

    private MetricsIdVO() {
    }
}
