package wjh.projects.infrastructure.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class MetricsDO {
    private Integer id;
    private String application;
    private String group;
    private String dataKey;
    private Double dataValue1;
    private Double dataValue2;
    private Date createTime;
}
