package ru.microservices.action_history_service.domain.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryActionDto {
    private Long serviceInstanceId;
    private String entityName;
    private String entityId;
    private Long count;

    public HistoryActionDto(
            Long serviceId,
            String entityName,
            Long count
    ) {
        this.serviceInstanceId = serviceId;
        this.entityName = entityName;
        this.count = count;
    }
}
