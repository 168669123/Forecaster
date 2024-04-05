package wjh.projects.infrastructure.dao;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@NoArgsConstructor
public class MetricsEstimateDO {
    private Integer id;
    private String recordKey;
    private Integer duration;
    private Integer deviation;
    private Date createTime;
}
