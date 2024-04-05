package wjh.projects.domain.transportVehicle.model.aggregate;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.util.DateUtil;
import wjh.projects.common.util.SpringContextUtil;
import wjh.projects.domain.base.AggregateRoot;
import wjh.projects.domain.transportVehicle.model.entity.TransportTask;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;
import wjh.projects.domain.transportVehicle.rpc.LocationFacadeClient;

import java.util.*;

/**
 * 运输车辆
 */
@Setter
public class TransportVehicle implements AggregateRoot<TransportVehicleIdVO> {
    private static final Logger logger = LoggerFactory.getLogger(TransportVehicle.class);

    private TransportVehicleIdVO transportVehicleIdVO;
    /**
     * 平均行驶速度，单位 km/h
     */
    private Double averageSpeed;
    /**
     * 运输车辆实时信息
     */
    private TransportVehicleMessageVO message;
    /**
     * 车辆运输任务
     */
    private List<TransportTask> transportTasks;

    private TransportVehicle() {
    }

    public TransportVehicle(TransportVehicleIdVO id, TransportVehicleMessageVO message) {
        this.transportVehicleIdVO = id;
        this.message = message;
    }

    /**
     * 获取运输任务目的地的地址
     */
    public String getDestinationAddress(String transportTaskId) {
        TransportTask transportTask = getTransportTask(transportTaskId);
        return transportTask.getDestinationAddress();
    }

    /**
     * 获取运输车辆的所有运输任务 id
     */
    public List<String> getTransportTaskIds() {
        List<String> transportTaskIds = new ArrayList<>();
        for (TransportTask transportTask : transportTasks)
            transportTaskIds.add(transportTask.getId());

        return transportTaskIds;
    }

    /**
     * 删除已经到车的运输任务
     */
    public void deleteArrivedTransportTasks() {
        logger.info("删除车辆：{} 已经到车的运输任务", getId());
        transportTasks.removeIf(TransportTask::isArrived);
        SpringContextUtil.getBean(TransportVehicleRepository.class).save(this);
    }

    /**
     * 获取运输车辆 id
     */
    public String getId() {
        return transportVehicleIdVO.getId();
    }

    /**
     * 根据运输任务 id 获取运输任务
     */
    private TransportTask getTransportTask(String transportTaskId) {
        for (TransportTask transportTask : transportTasks) {
            if (transportTask.getId().equals(transportTaskId))
                return transportTask;
        }
        throw new RuntimeException("运输任务id\"" + transportTaskId + "\"错误，未找到相应的运输任务！");
    }

    /**
     * 计算从 currentLocation 到车辆运输任务中 unArrivedSites 的距离
     */
    public Map<Integer, Double> calculateDistanceMap(String transportTaskId) {
        logger.info("开始计算当前位置到车辆运输任务：{} 中还未到车站点的里程", transportTaskId);
        LocationVO currentLocation = message.getLocation();
        LinkedHashMap<Integer, LocationVO> unArrivedLocations = getTransportTask(transportTaskId).getUnArrivedLocations();
        LocationFacadeClient locationFacadeClient = SpringContextUtil.getBean(LocationFacadeClient.class);
        return locationFacadeClient.calculate(currentLocation, unArrivedLocations);
    }

    /**
     * 获取车辆运输任务的发车站点 id
     */
    public int getSendSiteId(String transportTaskId) {
        TransportTask transportTask = getTransportTask(transportTaskId);
        return transportTask.getSendSiteId();
    }

    /**
     * 获取当前 siteId 的上一个站点 id
     * 如果当前 siteId 为发车站点 id，则返回当前 siteId
     */
    public int getPreSiteId(String transportTaskId, int siteId) {
        TransportTask transportTask = getTransportTask(transportTaskId);
        return transportTask.getPreSiteId(siteId);
    }

    /**
     * 获取运输车辆实时信息的发送时间
     */
    public Date getMessageSendTime() {
        return message.getSendTime();
    }

    /**
     * 更新车辆运输任务
     */
    public void updateTransportTasks() {
        logger.info("更新车辆：{} 运输任务中各站点的状态", getId());
        for (TransportTask transportTask : transportTasks)
            transportTask.updateRoute(message);

        SpringContextUtil.getBean(TransportVehicleRepository.class).save(this);
    }

    /**
     * 获取运输车辆当前的经纬度信息
     *
     * @return [latitude, longitude]
     */
    public double[] getCurrentGeo() {
        LocationVO location = message.getLocation();
        return new double[]{location.getLatitude(), location.getLongitude()};
    }

    /**
     * 获取车辆运输任务中各站点预计到与计划到的时间偏差
     */
    public int getTimeDeviation(String transportTaskId, String address, int duration) {
        for (TransportTask transportTask : transportTasks) {
            if (!transportTask.getId().equals(transportTaskId))
                continue;

            Date planTime = transportTask.getPlanTime(address);
            Date estimateTime = DateUtil.addSeconds(message.getSendTime(), duration);
            long deviation = (estimateTime.getTime() - planTime.getTime()) / 1000;
            return (int) deviation;
        }

        throw new RuntimeException("车辆运输任务：" + transportTaskId + "不存在");
    }
}