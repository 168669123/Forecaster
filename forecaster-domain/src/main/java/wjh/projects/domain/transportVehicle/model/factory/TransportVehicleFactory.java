package wjh.projects.domain.transportVehicle.model.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wjh.projects.common.constants.PropertiesEnum;
import wjh.projects.common.util.PropertiesUtil;
import wjh.projects.common.util.SpringContextUtil;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;
import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleIdVO;
import wjh.projects.domain.transportVehicle.model.vo.TransportVehicleMessageVO;
import wjh.projects.domain.transportVehicle.repository.TransportVehicleRepository;

import java.util.Date;

public class TransportVehicleFactory {
    private static final Logger logger = LoggerFactory.getLogger(TransportVehicleFactory.class);

    public static TransportVehicle create(TransportVehicleInfo transportVehicleInfo) {
        if (!validate(transportVehicleInfo.getCreateTime(), transportVehicleInfo.getGpsTime()))
            return null;

        LocationVO locationVO = new LocationVO(
                transportVehicleInfo.getAddress(),
                transportVehicleInfo.getLatitude(),
                transportVehicleInfo.getLongitude());

        TransportVehicleMessageVO message = new TransportVehicleMessageVO(
                locationVO,
                transportVehicleInfo.getSpeed(),
                transportVehicleInfo.getGpsTime(),
                transportVehicleInfo.getCreateTime());

        TransportVehicleIdVO transportVehicleIdVO = new TransportVehicleIdVO(transportVehicleInfo.getCarCode());
        TransportVehicle transportVehicle = new TransportVehicle(transportVehicleIdVO, message, null);
        SpringContextUtil.getBean(TransportVehicleRepository.class).assignTransportTasks(transportVehicle);
        return transportVehicle;
    }

    /**
     * 判断运输车辆实时信息是否有效
     *
     * @return 当延迟上传时间 <= GPS 信息延时上传的阈值时，返回 true，否则返回 false
     */
    private static boolean validate(Date createTime, Date sendTime) {
        long delayTime = (createTime.getTime() - sendTime.getTime()) / (1000 * 60);
        int delayUploadThreshold = Integer.parseInt(PropertiesUtil.get(PropertiesEnum.GPS_DELAY_UPLOAD_THRESHOLD));
        boolean result = delayTime <= delayUploadThreshold;
        if (!result)
            logger.info("延迟时长过大，位置信息无效：{sendTime：{}, createTime:{}}", sendTime, createTime);

        return result;
    }
}
