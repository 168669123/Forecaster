package wjh.projects.domain.estimateArrive.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.constants.PropertiesEnum;
import wjh.projects.common.util.DeepCopyUtil;
import wjh.projects.common.util.PropertiesUtil;
import wjh.projects.domain.base.AggregateRoot;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预计到
 */
@Setter
@ToString
@AllArgsConstructor
public class EstimateArrive implements AggregateRoot<EstimateArriveIdVO> {
    private static final Logger logger = LoggerFactory.getLogger(EstimateArrive.class);
    private EstimateArriveIdVO estimateArriveIdVO;
    private List<EstimateArriveInfoVO> estimateArriveInfoVOs;

    private EstimateArrive() {
    }

    public EstimateArriveIdVO getEstimateArriveIdVO() {
        return DeepCopyUtil.deepCopy(estimateArriveIdVO, EstimateArriveIdVO.class);
    }

    public List<EstimateArriveInfoVO> getEstimateArriveInfoVOs() {
        List<EstimateArriveInfoVO> deepCopy = new ArrayList<>();
        for (EstimateArriveInfoVO estimateArriveInfoVO : estimateArriveInfoVOs)
            deepCopy.add(DeepCopyUtil.deepCopy(estimateArriveInfoVO, EstimateArriveInfoVO.class));

        return deepCopy;
    }

    /**
     * 判断预计到是否达到计算间隔
     */
    public boolean isUpToEstimateInterval() {
        double currentInterval = (new Date().getTime() - getCalculateTime().getTime()) / (1000 * 60.0);
        int estimateInterval = Integer.parseInt(PropertiesUtil.get(PropertiesEnum.ESTIMATE_INTERVAL));
        boolean result = currentInterval >= estimateInterval;
        if (!result) {
            logger.info(
                    "{}_{}还未达到预计到计算间隔：当前间隔{}分钟 < {}分钟",
                    getTransportVehicleId(),
                    estimateArriveIdVO.getTransportTaskId(),
                    currentInterval,
                    estimateInterval);
        }
        return result;
    }

    /**
     * 获取计算预计到的时间日期
     */
    public Date getCalculateTime() {
        return estimateArriveInfoVOs.get(0).getCalculateTime();
    }

    /**
     * 获取运输车辆 id
     */
    public String getTransportVehicleId() {
        return estimateArriveIdVO.getTransportVehicleId();
    }
}
