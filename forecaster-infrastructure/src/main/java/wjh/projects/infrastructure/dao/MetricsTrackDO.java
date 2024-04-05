package wjh.projects.infrastructure.dao;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@NoArgsConstructor
public class MetricsTrackDO {
    private Integer id;
    private String recordKey;
    private Double latitude;
    private Double longitude;
    private Date createTime;
}
