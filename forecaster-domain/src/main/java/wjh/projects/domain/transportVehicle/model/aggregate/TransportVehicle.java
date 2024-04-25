package wjh.projects.domain.transportVehicle.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.util.DeepCopyUtil;
import wjh.projects.domain.base.AggregateRoot;
import wjh.projects.domain.transportVehicle.model.entity.TransportTask;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 运输车辆
 */
@Setter
@AllArgsConstructor
public class TransportVehicle implements AggregateRoot<TransportVehicleIdVO> {
    private static final Logger logger = LoggerFactory.getLogger(TransportVehicle.class);

    private TransportVehicleIdVO transportVehicleIdVO;
    /**
     * 运输车辆实时信息
     */
    private TransportVehicleMessageVO transportVehicleMessageVO;
    /**
     * 车辆运输任务
     */
    private List<TransportTask> transportTasks;

    private TransportVehicle() {
    }

    public TransportVehicleIdVO getTransportVehicleIdVO() {
        return DeepCopyUtil.deepCopy(transportVehicleIdVO, TransportVehicleIdVO.class);
    }

    public TransportVehicleMessageVO getTransportVehicleMessageVO() {
        return DeepCopyUtil.deepCopy(transportVehicleMessageVO, TransportVehicleMessageVO.class);
    }

    public List<TransportTask> getTransportTasks() {
        List<TransportTask> deepCopy = new ArrayList<>();
        for (TransportTask transportTask : transportTasks)
            deepCopy.add(DeepCopyUtil.deepCopy(transportTask, TransportTask.class));

        return deepCopy;
    }
}