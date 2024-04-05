package wjh.projects.domain.transportVehicle.middleware;

import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

import java.util.function.Consumer;

/**
 * 运输车辆中间件服务依赖接口
 */
public interface TransportVehicleClient {

    /**
     * 消费运输车辆实时信息
     */
    void consumeTransportVehicleInfo(Consumer<TransportVehicle> consumer);

    /**
     * 发送车辆运输任务到车信息
     */
    void sendTransportTaskArriveInfo(String info);
}
