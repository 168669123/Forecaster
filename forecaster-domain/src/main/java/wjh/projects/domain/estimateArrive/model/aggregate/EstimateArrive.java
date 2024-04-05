package wjh.projects.domain.estimateArrive.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.Environment;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.base.AggregateRoot;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;

import java.util.*;

/**
 * 预计到
 */
@ToString
@AllArgsConstructor
public class EstimateArrive implements AggregateRoot<EstimateArriveIdVO> {
    private static final Logger logger = LoggerFactory.getLogger(EstimateArrive.class);
    private EstimateArriveIdVO estimateArriveIdVO;
    private List<EstimateArriveInfoVO> estimateArriveInfoVOs;

    private EstimateArrive() {
    }

    public String getId() {
        return estimateArriveIdVO.getId();
    }

    /**
     * 判断预计到是否达到计算间隔
     */
    public boolean isUpToEstimateInterval() {
        double currentInterval = (new Date().getTime() - getCalculateTime().getTime()) / (1000 * 60.0);
        int estimateInterval = Environment.getEstimateInterval();
        boolean result = currentInterval >= estimateInterval;
        if (!result) {
            logger.info(
                    "{}_{}还未达到预计到计算间隔：当前间隔{}分钟 < {}分钟",
                    getTransportVehicleId(),
                    getId(),
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
     * 获取预计耗时信息
     *
     * @return key：目的地地址，value：预计耗时
     */
    public Map<String, Integer> getEstimateDurations() {
        Map<String, Integer> estimateDurations = new HashMap<>();
        for (EstimateArriveInfoVO estimateArriveInfoVO : estimateArriveInfoVOs) {
            long duration = estimateArriveInfoVO.getEstimateArriveTime().getTime() - estimateArriveInfoVO.getGpsTime().getTime();
            estimateDurations.put(estimateArriveInfoVO.getAddress(), (int) (duration / 1000));
        }
        return estimateDurations;
    }

    /**
     * 将预计到信息集合转化为 json 字符串集合
     */
    public List<String> getJsonOfEstimateArriveInfoVOs() {
        List<String> jsonList = new ArrayList<>();
        for (EstimateArriveInfoVO estimateArriveInfoVO : estimateArriveInfoVOs)
            jsonList.add(JsonUtil.toJson(estimateArriveInfoVO));

        return jsonList;
    }

    /**
     * 获取运输车辆 id
     */
    public String getTransportVehicleId() {
        return estimateArriveInfoVOs.get(0).getTruckNumber();
    }
}
