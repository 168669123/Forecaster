package wjh.projects.facade.estimateArrive;

import wjh.projects.facade.estimateArrive.response.EstimateArriveDTO;
import wjh.projects.facade.estimateArrive.request.EstimateRequest;

import java.util.List;

/**
 * 预计到门面接口
 */
public interface EstimateArriveFacade {

    /**
     * 计算预计到
     */
    List<EstimateArriveDTO> estimate(EstimateRequest estimateRequest);
}
