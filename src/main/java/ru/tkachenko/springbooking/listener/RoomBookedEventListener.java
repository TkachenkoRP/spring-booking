package ru.tkachenko.springbooking.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.tkachenko.springbooking.dto.Event;
import ru.tkachenko.springbooking.dto.RoomBookedEvent;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;
import ru.tkachenko.springbooking.service.KafkaService;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoomBookedEventListener {
    private final KafkaService<Event> kafkaService;

    @KafkaListener(topics = "${app.kafka.kafkaRoomBookedEventTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenRoomBookedEvent(@Payload RoomBookedEvent event,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                      @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.info("Received message: {}", event);
        log.info("Topic: {}, GroupId: {}", topic, groupId);
        kafkaService.add(event);
    }

    @KafkaListener(topics = "${app.kafka.kafkaUserRegistryEventTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenUserRegisteredEvent(@Payload UserRegisteredEvent event,
                                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                          @Header(KafkaHeaders.GROUP_ID) String groupId) {
        log.info("Received message: {}", event);
        log.info("Topic: {}, GroupId: {}", topic, groupId);
        kafkaService.add(event);
    }
}
