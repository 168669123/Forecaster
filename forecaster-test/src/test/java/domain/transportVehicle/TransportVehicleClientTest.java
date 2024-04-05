package domain.transportVehicle;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;
import wjh.projects.common.util.JsonUtil;
import wjh.projects.domain.transportVehicle.middleware.TransportVehicleClient;
import wjh.projects.domain.transportVehicle.model.aggregate.TransportVehicle;

import javax.annotation.Resource;
import java.util.function.Consumer;

@SpringBootTest(classes = ForecasterApplication.class)
public class TransportVehicleClientTest {
    private final static Logger logger = LoggerFactory.getLogger(TransportVehicleClientTest.class);
    @Resource
    private TransportVehicleClient transportVehicleClient;

    @Test
    public void testSendTransportTaskArriveInfo() {
        transportVehicleClient.sendTransportTaskArriveInfo("testInfo");
        try {
            // 等待发送结果
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConsumeTransportVehicleInfo() {
        Consumer<TransportVehicle> consumer = transportVehicle -> {
            logger.info("成功消费信息：{}", JsonUtil.toJson(transportVehicle));
        };
        transportVehicleClient.consumeTransportVehicleInfo(consumer);
        try {
            // 等待消费结果
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}