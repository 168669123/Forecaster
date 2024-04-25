package wjh.projects.interfaces.worker;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wjh.projects.common.constants.KafkaTopicConst;
import wjh.projects.common.util.FileUtil;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * 运输车辆信息生产模拟器
 */
@Component
public class TransportVehicleInfoProduceSimulator {
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedRate = 20000)
    public void produce() {
        String[] filePaths = new String[]{
                "data/track_text_1709709469905.txt",
                "data/track_text_1709709496451.txt",
                "data/track_text_1709709526867.txt"};
        for (int i = 0; i < filePaths.length; i++) {
            URL resource = this.getClass().getClassLoader().getResource(filePaths[i]);
            try {
                filePaths[i] = URLDecoder.decode(Objects.requireNonNull(resource).getPath(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        for (String filePath : filePaths) {
            String jsonArray;
            if ((jsonArray = FileUtil.getInstance().getFixedSizeInTurnFromJsonArray(filePath, 1)) != null) {
                JSONObject data = new JSONArray(jsonArray).getJSONObject(0);
                kafkaTemplate.send(KafkaTopicConst.TRANSPORT_VEHICLE_INFO, data.toString());
            }
        }
    }
}
