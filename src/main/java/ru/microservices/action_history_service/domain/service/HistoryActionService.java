package ru.microservices.action_history_service.domain.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.payload.HistoryActionDto;
import ru.microservices.action_history_service.domain.payload.HistoryActionMessage;
import ru.microservices.action_history_service.domain.repository.HistoryActionDao;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class HistoryActionService {

    private final HistoryActionDao historyActionDao;

    public List<HistoryAction> getByServiceIdAndEntityName(
            Long serviceId,
            String entityName,
            Pageable pageable
    ) {
        return Objects.requireNonNull(
                        historyActionDao.getByServiceIdAndEntityName(
                                        serviceId,
                                        entityName,
                                        pageable
                                )
                                .block()
                )
                .toList();
    }

    public List<HistoryAction> getByServiceIdAndEntityNameAndEntityId(
            Long serviceId,
            String entityName,
            String entityId,
            Pageable pageable
    ) {
        return Objects.requireNonNull(
                        historyActionDao.getByServiceIdEntityNameEntityId(
                                        serviceId,
                                        entityName,
                                        entityId,
                                        pageable
                                )
                                .block()
                )
                .toList();
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
