package wjh.projects.domain.transportVehicle.model.entity;

import lombok.Data;
import wjh.projects.domain.base.Entity;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;

import java.util.Date;

/**
 * 运输站点
 */
@Data
public class Site implements Entity<SiteIdVO> {
    private SiteIdVO siteIdVO;
    /**
     * 下一个站点
     */
    private Site next;
    /**
     * 当前站点的计划送达时间
     */
    private Date planTime;
    /**
     * 当前站点是否到车
     */
    private boolean arrived;
    /**
     * 站点位置信息
     */
    private LocationVO location;

    private Site() {
    }

    public int getId() {
        return siteIdVO.getId();
    }

    public Site(SiteIdVO siteIdVO, Date planTime, LocationVO location) {
        this.siteIdVO = siteIdVO;
        this.planTime = planTime;
        this.location = location;
    }
}
