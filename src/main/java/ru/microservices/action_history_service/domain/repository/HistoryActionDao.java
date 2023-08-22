package ru.microservices.action_history_service.domain.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.payload.HistoryActionDto;
import ru.microservices.action_history_service.domain.payload.HistoryActionMessage;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryActionDao {

    private final HistoryActionRepository historyActionRepository;

    public Mono<Page<HistoryAction>> getByServiceIdEntityNameEntityId(
            Long serviceId,
            String entityName,
            String entityId,
            Pageable pageable
    ) {
        // todo if
        return historyActionRepository
                .findAllByServiceInstanceIdAndEntityNameAndEntityId(
                        serviceId,
                        entityName,
                        entityId,
                        pageable
                )
                .collectList()
                .zipWith(
                        historyActionRepository.count()
                )
                .map(o -> new PageImpl<>(
                                o.getT1(),
                                pageable,
                                o.getT2()
                        )
                );
    }

    public Mono<Page<HistoryAction>> getByServiceIdAndEntityName(
            Long serviceId,
            String entityName,
            Pageable pageable
    ) {
        // todo if
        return historyActionRepository
                .findAllByServiceInstanceIdAndEntityName(
                        serviceId,
                        entityName,
                        pageable
                )
                .collectList()
                .zipWith(
                        historyActionRepository.count()
                )
                .map(o -> new PageImpl<>(
                                o.getT1(),
                                pageable,
                                o.getT2()
                        )
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
