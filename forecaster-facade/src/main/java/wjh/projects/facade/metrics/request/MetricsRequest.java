package wjh.projects.facade.metrics.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class MetricsRequest {
    @NotNull(message = "应用名不能为空")
    private String application;
    @NotNull(message = "数据分组不能为空")
    private String group;
    private String dataKey;
}
