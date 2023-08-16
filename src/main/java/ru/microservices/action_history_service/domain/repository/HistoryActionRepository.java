package ru.microservices.action_history_service.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.microservices.action_history_service.domain.entity.HistoryAction;

public interface HistoryActionRepository extends ReactiveCrudRepository<HistoryAction, Long> {
}
