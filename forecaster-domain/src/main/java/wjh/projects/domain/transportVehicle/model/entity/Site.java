package wjh.projects.domain.transportVehicle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wjh.projects.common.util.GeoUtil;
import wjh.projects.domain.base.Entity;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;

import java.util.Date;

/**
 * 运输站点
 */
@Getter
@AllArgsConstructor
public class Site implements Entity<SiteIdVO> {
    /**
     * 唯一标识
     */
    private SiteIdVO siteIdVO;
    /**
     * 当前站点的计划送达时间
     */
    private Date planTime;
    /**
     * 当前站点是否到车
     */
    private Boolean arrived;
    /**
     * 下一个站点
     */
    private Site next;

    private Site() {
    }

    public boolean equalsId(SiteIdVO id) {
        if (id.getSiteId() != siteIdVO.getSiteId())
            return false;

        return id.getLocationVO().getAddress().equals(siteIdVO.getLocationVO().getAddress());
    }

    /**
     * 判断当前运输站点是否是运输任务终点
     */
    public boolean isTerminus() {
        return next == null;
    }

    /**
     * 根据当前位置信息判断站点是否到车
     */
    public boolean isArrived(LocationVO currLocation) {
        LocationVO siteLocation = siteIdVO.getLocationVO();
        double distance = GeoUtil.calculateLinearDistance(
                currLocation.getLongitude(),
                siteLocation.getLongitude(),
                currLocation.getLatitude(),
                siteLocation.getLatitude());

        // 如果当前位置和站点间的距离 <= 100 m，则认为该站点已经到车
        if (!getArrived() && distance <= 100)
            arrived = true;

        return arrived;
    }
}
