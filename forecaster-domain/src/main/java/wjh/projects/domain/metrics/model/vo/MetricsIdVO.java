package wjh.projects.domain.metrics.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
@AllArgsConstructor
public class MetricsIdVO implements Identifier {
    private int id;

    private MetricsIdVO() {
    }
}
