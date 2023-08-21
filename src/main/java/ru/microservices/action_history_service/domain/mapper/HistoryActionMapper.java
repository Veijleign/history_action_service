package ru.microservices.action_history_service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.microservices.action_history_service.HistoryActionByServiceAndEntityIdRequest;
import ru.microservices.action_history_service.HistoryActionByServiceAndEntityNameRequest;
import ru.microservices.action_history_service.HistoryActionByServiceResponse;
import ru.microservices.action_history_service.HistoryActionModel;
import ru.microservices.action_history_service.domain.entity.HistoryAction;
import ru.microservices.action_history_service.domain.payload.HistoryActionDto;

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
                .setEntityId(historyAction.getId())
                .setEntityName(historyAction.getEntityName())
                .setFieldName(historyAction.getFieldName())
                .setPastValueName(historyAction.getPastValue())
                .setNewValueName(historyAction.getNewValue())
                .setUpdatedAt(historyAction.getUpdatedAt().toString())
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

    public HistoryActionDto toHistoryActionDto(HistoryActionByServiceAndEntityIdRequest request) {
        return new HistoryActionDto(
                request.getServiceId(),
                request.getEntityName(),
                request.getEntityId(),
                request.getCount()
        );
    }

    public HistoryActionDto toHistoryActionDto(HistoryActionByServiceAndEntityNameRequest request) {
        return new HistoryActionDto(
                request.getServiceId(),
                request.getEntityName(),
                request.getCount()
        );
    }



}
