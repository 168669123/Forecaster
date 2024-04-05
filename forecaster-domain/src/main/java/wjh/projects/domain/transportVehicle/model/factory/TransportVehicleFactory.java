package wjh.projects.domain.transportVehicle.model.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.Environment;
import wjh.projects.common.util.SpringContextUtil;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransportVehicleFactory {
    private static final Logger logger = LoggerFactory.getLogger(TransportVehicleFactory.class);
    private static volatile TransportVehicleFactory transportVehicleFactory;
    private final Map<String, TransportVehicle> cache;

    private TransportVehicleFactory() {
        cache = new HashMap<>();
    }

    public static TransportVehicleFactory getInstance() {
        if (transportVehicleFactory == null) {
            synchronized (TransportVehicleFactory.class) {
                if (transportVehicleFactory == null) {
                    transportVehicleFactory = new TransportVehicleFactory();
                }
            }
        }
        return transportVehicleFactory;
    }

    public static TransportVehicle create(String transportVehicleId, String address, double latitude, double longitude,
                                          double speed, Date sendTime, Date createTime) {
        // 判断运输车辆实时信息是否有效
        if (!validate(createTime, sendTime))
            return null;

        // 从缓存中获取运输车辆
        TransportVehicle transportVehicle = getInstance().getFromCache(transportVehicleId);
        LocationVO location = new LocationVO(address, latitude, longitude);
        TransportVehicleMessageVO message = new TransportVehicleMessageVO(location, speed, sendTime, createTime);

        // 如果缓存中存在目标运输车辆，则只需更新其中的运输车辆实时信息
        if (transportVehicle != null) {
            logger.info("当前缓存中存在目标运输车辆：{}，更新车辆实时位置信息", transportVehicleId);
            transportVehicle.setMessage(message);
        } else {
            logger.info("当前缓存中不存在目标运输车辆：{}，查询车辆实时运输任务后放入缓存", transportVehicleId);
            TransportVehicleIdVO id = new TransportVehicleIdVO(transportVehicleId);
            transportVehicle = new TransportVehicle(id, message);
            // 为运输车辆的实时运输任务赋值
            SpringContextUtil.getBean(TransportVehicleRepository.class).assignTransportTasks(transportVehicle);
            // 将运输车辆放进缓存
            getInstance().putInCache(transportVehicleId, transportVehicle);
        }

        // 更新车辆运输任务
        transportVehicle.updateTransportTasks();
        // 删除已经到车的运输任务
        transportVehicle.deleteArrivedTransportTasks();
        return transportVehicle;
    }

    /**
     * 判断运输车辆实时信息是否有效
     *
     * @return 当延迟上传时间 <= GPS 信息延时上传的阈值时，返回 true，否则返回 false
     */
    private static boolean validate(Date createTime, Date sendTime) {
        long delayTime = (createTime.getTime() - sendTime.getTime()) / (1000 * 60);
        boolean result = delayTime <= Environment.getGpsDelayUploadThreshold();
        if (!result)
            logger.info("延迟时长过大，位置信息无效：{sendTime：{}, createTime:{}}", sendTime, createTime);

        return result;
    }

    private TransportVehicle getFromCache(String transportVehicleId) {
        return cache.get(transportVehicleId);
    }

    private void putInCache(String transportVehicleId, TransportVehicle transportVehicle) {
        cache.put(transportVehicleId, transportVehicle);
    }
}
