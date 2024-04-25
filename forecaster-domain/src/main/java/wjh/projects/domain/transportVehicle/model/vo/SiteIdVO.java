package wjh.projects.domain.transportVehicle.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.domain.base.Identifier;

@Getter
@AllArgsConstructor
public class SiteIdVO implements Identifier {
    /**
     * 唯一标识
     */
    private Integer siteId;
    /**
     * 站点位置信息
     */
    private LocationVO locationVO;

    private SiteIdVO() {
    }
}
