package ru.tkachenko.springbooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractTestController {

    protected static final String USER_AUTHORIZATION = "Basic " + Base64.getEncoder().encodeToString("User_Name_1:111".getBytes());
    protected static final String KAFKA_GROUP = "test-group-id";
    protected static final String KAFKA_BOOKING_TOPIC = "test-booking-topic";
    protected static final String KAFKA_USER_TOPIC = "test-user-topic";

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:12.3");

    @Container
    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:6.2.0")
    );

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("app.kafka.kafkaMessageGroupId", () -> "kafka-app-group-id");
        registry.add("app.kafka.kafkaRoomBookedEventTopic", () -> KAFKA_BOOKING_TOPIC);
        registry.add("app.kafka.kafkaUserRegistryEventTopic", () -> KAFKA_USER_TOPIC);
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T getKafkaMessage(Class<T> messageType, String topic) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(kafka.getBootstrapServers(), KAFKA_GROUP, "true");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ConsumerFactory<String, T> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new JsonDeserializer<>(messageType, objectMapper));
        Consumer<String, T> consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singleton(topic));
        ConsumerRecord<String, T> receivedRecord = KafkaTestUtils.getSingleRecord(consumer, topic);
        return receivedRecord.value();
    }
}

