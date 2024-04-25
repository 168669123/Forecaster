package wjh.projects.domain.metrics.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@AllArgsConstructor
public class DataVO implements Serializable {
    private Double dataValue1;
    private Double dataValue2;
    private Date createTime;

    private DataVO() {
    }
}
