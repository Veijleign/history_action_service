package ru.microservices.action_history_service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import ru.microservices.action_history_service.HistoryActionByServiceRequest;
import ru.microservices.action_history_service.HistoryActionByServiceResponse;
import ru.microservices.action_history_service.HistoryActionLastByServiceResponse;
import ru.microservices.action_history_service.HistoryActionServiceGrpc;
import ru.microservices.action_history_service.domain.mapper.HistoryActionMapper;
import ru.microservices.action_history_service.domain.service.HistoryActionService;
import ru.microservices.action_history_service.util.StreamObserverUtils;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class HistoryServiceGrpc extends HistoryActionServiceGrpc.HistoryActionServiceImplBase {

    private final HistoryActionService historyActionService;
    private final HistoryActionMapper historyActionMapper;

    @Override
    public void getHistoryActionByService(HistoryActionByServiceRequest request,
                                          StreamObserver<HistoryActionByServiceResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> historyActionMapper.toHistoryActionByServiceResponse(
                        historyActionService.getHistoryActionByService(
                                request.getServiceId()
                        )
                )
        );
    }

    @Override
    public void getHistoryActionLastByService(HistoryActionByServiceRequest request,
                                              StreamObserver<HistoryActionLastByServiceResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> historyActionMapper.toHistoryActionLastByServiceResponse(
                        historyActionService.getLastHistoryActionByService(
                                request.getServiceId()
                        )
                )
        );
    }
}
