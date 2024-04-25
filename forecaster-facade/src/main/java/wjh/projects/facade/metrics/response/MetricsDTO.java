package wjh.projects.facade.metrics.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MetricsDTO {
    private String dataKey;
    private Double dataValue1;
    private Double dataValue2;
    private Date createTime;
}
