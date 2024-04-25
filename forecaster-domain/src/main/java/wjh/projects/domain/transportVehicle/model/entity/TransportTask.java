package wjh.projects.domain.transportVehicle.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.base.Entity;
import wjh.projects.domain.transportVehicle.model.vo.SiteIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportTaskIdVO;

import java.util.Date;
import java.util.Map;

/**
 * 车辆运输任务
 */
@Getter
@AllArgsConstructor
public class TransportTask implements Entity<TransportTaskIdVO> {
    private static final Logger logger = LoggerFactory.getLogger(TransportTask.class);
    /**
     * 唯一标识
     */
    private TransportTaskIdVO transportTaskIdVO;
    /**
     * 运输路线
     */
    private Site route;
    /**
     * 当前位置到各个未到车运输站点的里程，单位：m
     */
    private Map<Site, Double> mileages;

    private TransportTask() {
    }

    /**
     * 获取运输任务目的地地址
     */
    public String getDestinationAddress() {
        Site site = route;
        while (site.getNext() != null)
            site = site.getNext();

        return site.getSiteIdVO().getLocationVO().getAddress();
    }

    /**
     * 获取车辆运输任务发车站点
     */
    public Site getSendSite() {
        return route;
    }

    /**
     * 获取车辆运输任务上一处站点
     *
     * @return 如果当前站点是发车站点，则返回当前站点
     */
    public Site getPreSite(SiteIdVO siteId) {
        Site prev = route;
        Site site = route;
        while (site != null && !site.equalsId(siteId)) {
            prev = site;
            site = site.getNext();
        }
        return prev;
    }

    /**
     * 获取运输站点的计划到时间
     */
    public Date getPlanTime(SiteIdVO id) {
        for (Site site = route; site != null; site = site.getNext()) {
            if (site.equalsId(id))
                return site.getPlanTime();
        }
        logger.info("未找到目标站点，当前运输任务：{}，目标站点：{}",
                transportTaskIdVO.getTransportTaskId(),
                JsonUtil.toJson(id));

        return null;
    }
}
