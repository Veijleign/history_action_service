package ru.microservices.action_history_service.domain.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.payload.HistoryActionMessage;
import ru.microservices.action_history_service.domain.repository.HistoryActionDao;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class HistoryActionService {

    private final HistoryActionDao historyActionDao;

    public List<HistoryAction> getHistoryActionByService(Long serviceInstanceId) {
        return historyActionDao.getAllById(serviceInstanceId)
                .collectList()
                .block();
    }

    public HistoryAction getLastHistoryActionByService(Long serviceInstanceId) {
        return historyActionDao.getLastById(serviceInstanceId)
                .block();
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.create-history-action-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void createNewRecord(
            @Payload HistoryActionMessage historyActionMessage,
            @org.springframework.messaging.handler.annotation.Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ) {
        log.info("From partition: " + partition);

        if (!historyActionMessage.changes().isEmpty()) {
            historyActionDao.createNewMessage(
                    historyActionMessage.serviceId(),
                    historyActionMessage.changes(),
                    historyActionMessage.updatedAt()
            ).block();
        }
    }

}
