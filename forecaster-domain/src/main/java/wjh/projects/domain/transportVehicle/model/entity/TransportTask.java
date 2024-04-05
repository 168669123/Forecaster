package wjh.projects.domain.transportVehicle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.util.GeoUtil;
import wjh.projects.common.util.SpringContextUtil;
import wjh.projects.domain.base.Entity;
import wjh.projects.domain.transportVehicle.middleware.TransportVehicleClient;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportTaskIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 车辆运输任务
 */
@Getter
@AllArgsConstructor
public class TransportTask implements Entity<TransportTaskIdVO> {
    private static final Logger logger = LoggerFactory.getLogger(TransportTask.class);
    private TransportTaskIdVO transportTaskIdVO;
    /**
     * 运输路线
     */
    private Site route;

    private TransportTask() {
    }

    public String getId() {
        return transportTaskIdVO.getId();
    }

    /**
     * 判断运输任务是否到车
     */
    public boolean isArrived() {
        Site site = route;
        while (site.getNext() != null)
            site = site.getNext();

        return site.isArrived();
    }

    /**
     * 获取运输任务中还未到车的站点位置信息
     */
    public LinkedHashMap<Integer, LocationVO> getUnArrivedLocations() {
        LinkedHashMap<Integer, LocationVO> unArrivedLocations = new LinkedHashMap<>();

        for (Site site = route; site != null; site = site.getNext()) {
            if (!site.isArrived())
                unArrivedLocations.put(site.getId(), site.getLocation());
        }

        return unArrivedLocations;
    }

    /**
     * 获取运输任务目的地的地址
     */
    public String getDestinationAddress() {
        Site site = route;
        while (site.getNext() != null)
            site = site.getNext();

        return site.getLocation().getAddress();
    }

    /**
     * 获取车辆运输任务的发车站点 id
     */
    public int getSendSiteId() {
        return route.getId();
    }

    /**
     * 获取当前 siteId 的上一个站点 id
     * 如果当前 siteId 为发车站点 id，则返回当前 siteId
     */
    public int getPreSiteId(int siteId) {
        Site site = route;
        int prev = site.getId();
        while (site != null) {
            if (site.getId() == siteId)
                break;

            prev = site.getId();
            site = site.getNext();
        }
        return prev;
    }

    /**
     * 根据运输车辆实时信息更新运输路线状态
     */
    public void updateRoute(TransportVehicleMessageVO message) {
        LocationVO currLocation = message.getLocation();
        LocationVO siteLocation;

        for (Site site = route; site != null; site = site.getNext()) {
            siteLocation = site.getLocation();
            double distance = GeoUtil.calculateLinearDistance(
                    currLocation.getLongitude(),
                    siteLocation.getLongitude(),
                    currLocation.getLatitude(),
                    siteLocation.getLatitude());

            // 如果当前位置和站点间的距离 <= 100 m，则认为该站点已经到车
            if (!site.isArrived() && distance <= 100) {
                site.setArrived(true);
                // 如果是终点站到车，则发送运输任务到车信息
                if (site.getNext() == null){
                    logger.info("车辆运输任务：{} 终点站：{} 到车，发送运输任务到车信息", getId(), getDestinationAddress());
                    SpringContextUtil.getBean(TransportVehicleClient.class).sendTransportTaskArriveInfo(getId());
                }
            }
        }
    }

    /**
     * 获取运输站点的计划到时间
     */
    public Date getPlanTime(String address) {
        Site site = route;
        while (site != null) {
            if (site.getLocation().getAddress().equals(address))
                return site.getPlanTime();

            site = site.getNext();
        }

        return null;
    }
}
