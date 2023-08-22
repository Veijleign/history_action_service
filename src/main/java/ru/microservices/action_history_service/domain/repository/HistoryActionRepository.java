package ru.microservices.action_history_service.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.microservices.action_history_service.domain.entity.HistoryAction;

@Repository
public interface HistoryActionRepository
        extends R2dbcRepository<HistoryAction, Long> {

    Flux<HistoryAction> findAllByServiceInstanceIdAndEntityNameAndEntityId(
            Long serviceId,
            String entityName,
            String entityId,
            Pageable pageable
    );

    Flux<HistoryAction> findAllByServiceInstanceIdAndEntityName(
            Long serviceId,
            String entityName,
            Pageable pageable
    );
}
