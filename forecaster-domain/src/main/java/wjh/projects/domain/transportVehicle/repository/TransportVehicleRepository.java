package wjh.projects.domain.transportVehicle.repository;

import wjh.projects.domain.base.Repository;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;

public interface TransportVehicleRepository extends Repository<TransportVehicle, TransportVehicleIdVO> {

    /**
     * 为运输车辆的实时运输任务赋值
     */
    void assignTransportTasks(TransportVehicle transportVehicle);
}
