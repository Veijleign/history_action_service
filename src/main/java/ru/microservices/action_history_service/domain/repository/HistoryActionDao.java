package ru.microservices.action_history_service.domain.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.payload.HistoryActionDto;
import ru.microservices.action_history_service.domain.payload.HistoryActionMessage;
import ru.microservices.action_history_service.util.DatePatterns;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryActionDao {

    private final HistoryActionRepository historyActionRepository;

    public Flux<HistoryAction> getByServiceIdEntityNameEntityId(HistoryActionDto dto) {
        // todo if
        return historyActionRepository
                .findByServiceInstanceIdAndEntityAndEntityIdAndEntityName(
                        dto.getServiceInstanceId(),
                        dto.getEntityName(),
                        dto.getEntityId()
                );
    }

    public Flux<HistoryAction> getByServiceIdAndEntityName(HistoryActionDto dto) {
        // todo if
        return historyActionRepository
                .findByServiceInstanceIdAAndEntityName(
                        dto.getServiceInstanceId(),
                        dto.getEntityName()
                );
    }

    @Transactional
    public Mono<Void> createNewRecordInDatabase(
            HistoryActionMessage message
    ) {
        return historyActionRepository
                .save(
                        new HistoryAction(
                                null,
                                message.serviceId(),
                                message.entityId(),
                                message.entityName(),
                                message.fieldName(),
                                message.pastValue(),
                                message.newValue(),
                                LocalDateTime.parse(
                                        message.updatedAt()
                                )
                        )
                )
                .then();
    }

}
