package ru.microservices.action_history_service.domain.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.microservices.action_history_service.domain.entity.HistoryAction;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryActionDao {

    private final HistoryActionRepository historyActionRepository;

    public Flux<HistoryAction> getAllById(Long serviceInstanceId) {
        return historyActionRepository.findAllByServiceInstanceId(serviceInstanceId);
    }

    public Mono<HistoryAction> getLastById(Long serviceInstanceId) {
        // if
        return historyActionRepository.findTopByServiceInstanceId(serviceInstanceId);
    }

    @Transactional
    public Mono<Void> createNewMessage(
            Long serviceId,
            Map<String, Map<String, String>> changes,
            String updatedAt
    ) {


        Flux<HistoryAction> historyAction = Flux.fromIterable(changes.entrySet())
                .flatMap(entry ->
                        Flux.fromIterable(
                                        entry.getValue()
                                                .entrySet()
                                )
                                .flatMap(innerEntry -> Mono.just(
                                                new HistoryAction(
                                                        null,
                                                        serviceId,
                                                        entry.getKey(),
                                                        innerEntry.getKey(),
                                                        innerEntry.getValue(),
                                                        updatedAt)
                                        )
                                )
                );

        return historyActionRepository
                .saveAll(historyAction)
                .then();
    }

}
