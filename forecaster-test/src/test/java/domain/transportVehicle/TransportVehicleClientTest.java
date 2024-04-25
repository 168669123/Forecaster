package domain.transportVehicle;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wjh.projects.ForecasterApplication;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@SpringBootTest(classes = ForecasterApplication.class)
public class TransportVehicleClientTest {
    private KafkaConsumer<String, Object> consumer;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "47.120.70.3:9092");
        props.put("group.id", "test");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("transport_task_arrive"));
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void testSendTransportTaskArriveInfo() {
        String testInfo = "testInfo";
        ConsumerRecords<String, Object> records = consumer.poll(Duration.ofSeconds(5));

        Assertions.assertTrue(records.iterator().hasNext(), "没有在 Kafka 中发现新消息");
        ConsumerRecord<String, Object> record = records.iterator().next();
        Assertions.assertEquals("transport_task_arrive", record.topic());
        Assertions.assertEquals(testInfo, record.value());
    }
}