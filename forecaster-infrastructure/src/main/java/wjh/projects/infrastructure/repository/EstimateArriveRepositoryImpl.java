package wjh.projects.infrastructure.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.common.util.StringUtil;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveIdVO;
import wjh.projects.domain.estimateArrive.model.vo.EstimateArriveInfoVO;
import wjh.projects.domain.estimateArrive.repository.EstimateArriveRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EstimateArriveRepositoryImpl implements EstimateArriveRepository {
    private final static Logger logger = LoggerFactory.getLogger(EstimateArriveRepositoryImpl.class);
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public EstimateArriveRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(EstimateArrive estimateArrive) {
        EstimateArriveIdVO estimateArriveIdVO = estimateArrive.getEstimateArriveIdVO();
        String key = StringUtil.append(
                "truckNumber:",
                estimateArriveIdVO.getTransportVehicleId(),
                ":msgId:",
                estimateArriveIdVO.getTransportTaskId());

        redisTemplate.delete(key);
        for (EstimateArriveInfoVO estimateArriveInfoVO : estimateArrive.getEstimateArriveInfoVOs())
            redisTemplate.opsForList().rightPush(key, JsonUtil.toJson(estimateArriveInfoVO));

        logger.info("存储预计到信息，当前运输车辆：{}、运输任务：{}",
                estimateArriveIdVO.getTransportVehicleId(),
                estimateArriveIdVO.getTransportTaskId());
    }

    @Override
    public EstimateArrive query(EstimateArriveIdVO id) {
        String key = StringUtil.append(
                "truckNumber:",
                id.getTransportVehicleId(),
                ":msgId:",
                id.getTransportTaskId());

        List<Object> objects = redisTemplate.opsForList().range(key, 0, -1);
        if (objects != null && objects.size() != 0) {
            List<EstimateArriveInfoVO> estimateArriveInfoVOs = new ArrayList<>();
            for (Object object : objects) {
                String jsonString = StringUtil.toJSONString(object.toString());
                estimateArriveInfoVOs.add(JsonUtil.parseJson(jsonString, EstimateArriveInfoVO.class));
            }
            EstimateArriveIdVO estimateArriveIdVO = new EstimateArriveIdVO(
                    id.getTransportVehicleId(),
                    key.substring(key.lastIndexOf(":") + 1));

            return new EstimateArrive(estimateArriveIdVO, estimateArriveInfoVOs);
        }
        return null;
    }

    @Override
    public void delete(EstimateArriveIdVO estimateArriveIdVO) {
        String key = StringUtil.append(
                "truckNumber:",
                estimateArriveIdVO.getTransportVehicleId(),
                ":msgId:",
                estimateArriveIdVO.getTransportTaskId());

        redisTemplate.delete(key);
        logger.info("删除预计到信息，当前运输车辆：{}、运输任务：{}",
                estimateArriveIdVO.getTransportVehicleId(),
                estimateArriveIdVO.getTransportTaskId());
    }
}
