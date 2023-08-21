package ru.microservices.action_history_service.domain.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.microservices.action_history_service.domain.entity.HistoryAction;

@Repository
public interface HistoryActionRepository
        extends ReactiveCrudRepository<HistoryAction, Long> {

    @Query("""
            SELECT *
            FROM history_action item
            WHERE item.service_instance_id =:serviceInstanceId
            AND item.entity_id =:entityId
            AND item.entity_name =:entityName
            """
    )
    Flux<HistoryAction> findByServiceInstanceIdAndEntityAndEntityIdAndEntityName(
            Long serviceInstanceId,
            String entityName,
            String entityId
    );

    @Query(""" 
            SELECT *
            FROM history_action item
            WHERE item.service_instance_id =:serviceInstanceId
            AND item.entity_name =:entityName
            """
    )
    Flux<HistoryAction> findByServiceInstanceIdAAndEntityName(
            Long serviceInstanceId,
            String entityName
    );
}
