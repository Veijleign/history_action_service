package ru.microservices.action_history_service.domain.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.microservices.action_history_service.domain.entity.HistoryAction;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryActionDao {

    private final HistoryActionRepository historyActionRepository;

    public Flux<HistoryAction> getAllById(Long id) {

    }

    public Mono<HistoryAction> getLastById(Long id) {

    }

}
