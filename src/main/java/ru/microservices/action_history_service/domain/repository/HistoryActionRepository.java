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
            SELECT service_instance_id item
            FROM history_action
            WHERE item.service_instance_id =:serviceInstanceId
            """
    )
    Mono<HistoryAction> findTopByServiceInstanceId(Long serviceInstanceId);

    @Query("""
            SELECT *
            FROM history_action
            WHERE item.service_instance_id =:serviceInstanceId
            """
    )
    Flux<HistoryAction> findAllByServiceInstanceId(Long serviceInstanceId);
}
