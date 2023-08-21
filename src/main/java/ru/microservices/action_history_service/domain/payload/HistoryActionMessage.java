package ru.microservices.action_history_service.domain.payload;

public record HistoryActionMessage(
    Long serviceId,
    String entityId,
    String entityName,
    String fieldName,
    String pastValue,
    String newValue,
    String updatedAt
) {

}