package wjh.projects.domain.estimateArrive.middleware;

import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

import java.util.List;

/**
 * 预计到中间件服务依赖接口
 */
public interface EstimateArriveClient {

    /**
     * 获取运输车辆上次计算的预计到
     */
    List<EstimateArrive> listEstimateArrives(String transportVehicleId);

    /**
     * 存储运输车辆的预计到信息
     */
    void saveEstimateArrives(TransportVehicle transportVehicle, List<EstimateArrive> estimateArrives);

    /**
     * 清理已经到站的预计到信息
     */
    void clearEstimateArrives();
}
