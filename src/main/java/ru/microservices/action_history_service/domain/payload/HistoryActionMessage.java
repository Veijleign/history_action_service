package ru.microservices.action_history_service.domain.payload;

import java.util.Map;

public record HistoryActionMessage (
    Long serviceId,
    Map<String, Map<String, String>> changes,
    String updatedAt
) {

}