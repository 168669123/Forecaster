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
import wjh.projects.common.constants.KafkaGroupConst;
import wjh.projects.common.constants.KafkaTopicConst;
import wjh.projects.domain.estimateArrive.model.info.TransportTaskArriveInfo;

import java.util.Optional;

@Component
public class TransportTaskArriveInfoConsumer {
    private final EstimateArriveAppService estimateArriveAppService;

    @Autowired
    public TransportTaskArriveInfoConsumer(EstimateArriveAppService estimateArriveAppService) {
        this.estimateArriveAppService = estimateArriveAppService;
    }

    @KafkaListener(topics = KafkaTopicConst.TRANSPORT_TASK_ARRIVE_INFO, groupId = KafkaGroupConst.CONSUMER_1)
    public void getMessage(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.GROUP_ID) String group,
                           ConsumerRecord<?, ?> record,
                           Acknowledgment ack) {
        Optional<? extends ConsumerRecord<?, ?>> message = Optional.ofNullable(record);
        if (message.isPresent()) {
            JSONObject object = new JSONObject(message.get().value().toString());
            TransportTaskArriveInfo transportTaskArriveInfo = buildTransportTaskArriveInfo(object);
            estimateArriveAppService.clearEstimateArrives(transportTaskArriveInfo);
            ack.acknowledge();
        }
    }

    private TransportTaskArriveInfo buildTransportTaskArriveInfo(JSONObject object) {
        TransportTaskArriveInfo transportTaskArriveInfo = new TransportTaskArriveInfo();

        transportTaskArriveInfo.setTransportVehicleId(object.getString("transportVehicleId"));
        transportTaskArriveInfo.setTransportTaskId(object.getString("transportTaskId"));

        return transportTaskArriveInfo;
    }
}
