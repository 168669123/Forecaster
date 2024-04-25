package wjh.projects.interfaces.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import wjh.projects.application.service.EstimateArriveAppService;
import wjh.projects.application.service.MetricsAppService;
import wjh.projects.common.constants.KafkaGroupConst;
import wjh.projects.common.constants.KafkaTopicConst;
import wjh.projects.common.util.DateUtil;
import wjh.projects.domain.estimateArrive.model.aggregate.EstimateArrive;
import wjh.projects.domain.transportVehicle.model.info.TransportVehicleInfo;

import java.util.List;
import java.util.Optional;

@Component
public class TransportVehicleInfoConsumer {
    private final EstimateArriveAppService estimateArriveAppService;
    private final MetricsAppService metricsAppService;

    @Autowired
    public TransportVehicleInfoConsumer(EstimateArriveAppService estimateArriveAppService,
                                        MetricsAppService metricsAppService) {
        this.estimateArriveAppService = estimateArriveAppService;
        this.metricsAppService = metricsAppService;
    }

    @KafkaListener(topics = KafkaTopicConst.TRANSPORT_VEHICLE_INFO, groupId = KafkaGroupConst.CONSUMER_1)
    public void getMessage(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.GROUP_ID) String group,
                           ConsumerRecord<?, ?> record,
                           Acknowledgment ack) {
        Optional<? extends ConsumerRecord<?, ?>> message = Optional.ofNullable(record);
        if (message.isPresent()) {
            JSONObject object = new JSONObject(message.get().value().toString());
            TransportVehicleInfo transportVehicleInfo = buildTransportVehicleInfo(object);
            metricsAppService.trackTransportVehicle(transportVehicleInfo);
            List<EstimateArrive> estimateArrives = estimateArriveAppService.calculateEstimateArrive(transportVehicleInfo);
            metricsAppService.monitorEstimateArrive(transportVehicleInfo, estimateArrives);
            ack.acknowledge();
        }
    }

    private TransportVehicleInfo buildTransportVehicleInfo(JSONObject object) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        TransportVehicleInfo transportVehicleInfo = new TransportVehicleInfo();

        transportVehicleInfo.setCarCode(object.getString("carCode"));
        transportVehicleInfo.setLongitude(object.getDouble("longitude"));
        transportVehicleInfo.setLatitude(object.getDouble("latitude"));
        transportVehicleInfo.setGpsTime(DateUtil.stringToDate(object.getString("gpsTime"), pattern));
        transportVehicleInfo.setCreateTime(DateUtil.stringToDate(object.getString("createTime"), pattern));
        transportVehicleInfo.setSpeed(object.getDouble("speed"));
        transportVehicleInfo.setAddress(object.getString("address"));

        return transportVehicleInfo;
    }
}
