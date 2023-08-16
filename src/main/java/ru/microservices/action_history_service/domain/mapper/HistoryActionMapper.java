package ru.microservices.action_history_service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.microservices.action_history_service.HistoryActionByServiceResponse;
import ru.microservices.action_history_service.HistoryActionLastByServiceResponse;
import ru.microservices.action_history_service.HistoryActionModel;
import ru.microservices.action_history_service.domain.entity.HistoryAction;

import java.util.Collection;

@Component
public class HistoryActionMapper {

    public HistoryActionModel toHistoryActionModel(
            HistoryAction historyAction
    ) {
        return HistoryActionModel
                .newBuilder()
                .setId(historyAction.getId())
                .setServiceInstanceId(historyAction.getServiceInstanceId())
                .setEntity(historyAction.getEntity())
                .setField(historyAction.getField())
                .setValue(historyAction.getValue())
                .setUpdatedAt(historyAction.getUpdatedAt())
                .build();
    }
    public HistoryActionByServiceResponse toHistoryActionByServiceResponse(
            Collection<HistoryAction> historyActions
    ) {
        return HistoryActionByServiceResponse
                .newBuilder()
                .addAllHistoryActionModel(
                        historyActions
                                .stream()
                                .map(this::toHistoryActionModel)
                                .toList()
                )
                .build();
    }

    public HistoryActionLastByServiceResponse toHistoryActionLastByServiceResponse(
            HistoryAction historyAction
    ) {
        return HistoryActionLastByServiceResponse
                .newBuilder()
                .setHistoryActionModel(toHistoryActionModel(historyAction))
                .build();
    }


}
