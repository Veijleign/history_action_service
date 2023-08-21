package ru.microservices.action_history_service.domain.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.payload.HistoryActionDto;
import ru.microservices.action_history_service.domain.payload.HistoryActionMessage;
import ru.microservices.action_history_service.domain.repository.HistoryActionDao;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class HistoryActionService {

    private final HistoryActionDao historyActionDao;

    public List<HistoryAction> getByServiceIdAndEntityName(
            HistoryActionDto dto
    ) {
        return historyActionDao.getByServiceIdAndEntityName(dto)
                .collectList()
                .block();
    }

    public List<HistoryAction> getByServiceIdAndEntityNameAndEntityId(
            HistoryActionDto dto
    ) {
        return historyActionDao.getByServiceIdEntityNameEntityId(dto)
                .collectList()
                .block();
    }

    @KafkaListener(
            topics = "${spring.kafka.topics}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void createNewRecord(
            @Payload HistoryActionMessage historyActionMessage,
            @org.springframework.messaging.handler.annotation.Header(KafkaHeaders.RECEIVED_PARTITION) int partition
    ) {

        log.info("From partition: " + partition);
        log.info("Hello");

        historyActionDao.createNewRecordInDatabase(
                historyActionMessage
        ).block();
    }

}
