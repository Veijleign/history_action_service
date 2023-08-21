package ru.microservices.action_history_service.core.exception;

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import io.grpc.*;
import io.grpc.protobuf.StatusProto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.action_history_service.core.config.InstanceConfig;
import ru.microservices.action_history_service.util.DatePatterns;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GrpcExceptionHandler implements ServerInterceptor {

    private final InstanceConfig instanceConfig;

    @Override
    public <T, R> ServerCall.Listener<T> interceptCall(
            ServerCall<T, R> serverCall, Metadata headers, ServerCallHandler<T, R> serverCallHandler) {
        ServerCall.Listener<T> delegate = serverCallHandler.startCall(serverCall, headers);
        return new ExceptionHandler<>(delegate, serverCall,
                instanceConfig);
    }

    private static class ExceptionHandler<T, R>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<T> {

        private final InstanceConfig instanceConfig;

        private final ServerCall<T, R> delegate;


        ExceptionHandler(
                ServerCall.Listener<T> listener,
                ServerCall<T, R> serverCall,
                InstanceConfig instanceConfig
        ) {
            super(listener);
            this.delegate = serverCall;
            this.instanceConfig = instanceConfig;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (RuntimeException ex) {
                handleException(ex, delegate);
                throw ex;
            }
        }


        private void handleException(
                RuntimeException exception,
                ServerCall<T, R> serverCall) {

            Status status;
            Metadata headers;

            if (exception instanceof StatusRuntimeException) {

                StatusRuntimeException statusRuntimeException = (StatusRuntimeException) exception;
                com.google.rpc.Status extractedStatus = StatusProto.fromThrowable(statusRuntimeException);

                if (extractedStatus != null) {

                    List<Any> detailsList = new ArrayList<>(extractedStatus.getDetailsList());


                    GrpcError grpcError = GrpcError.INTERNAL;

                    Map<String, String> metaData = fillMetaData(
                            grpcError,
                            statusRuntimeException,
                            serverCall
                    );


                    ErrorInfo errorInfo = fillNewErrorInfo(
                            metaData,
                            grpcError
                    );


                    Any newDetail = Any.pack(errorInfo);

                    detailsList.add(newDetail);

                    com.google.rpc.Status newStatus = extractedStatus.toBuilder()
                            .setCode(
                                    grpcError
                                            .getGrpcStatus()
                                            .getCode()
                                            .value()
                            )
                            .clearDetails()
                            .addAllDetails(detailsList)
                            .build();

                    statusRuntimeException = StatusProto.toStatusRuntimeException(newStatus);
                }

                status = statusRuntimeException.getStatus();
                headers = statusRuntimeException.getTrailers();

            } else {

                GrpcException grpcException;

                if (exception instanceof GrpcException) {
                    grpcException = (GrpcException) exception;
                } else {
                    grpcException = GrpcException.of(exception);
                }

                GrpcError grpcError = grpcException.getError();

                Map<String, String> metaData = fillMetaData(
                        grpcError,
                        grpcException,
                        serverCall
                );

                ErrorInfo errorInfo = fillNewErrorInfo(
                        metaData,
                        grpcError
                );

                com.google.rpc.Status newStatus = com.google.rpc.Status.newBuilder()
                        .setCode(
                                grpcError
                                        .getGrpcStatus()
                                        .getCode()
                                        .value()
                        )
                        .addDetails(Any.pack(errorInfo))
                        .build();


                StatusRuntimeException statusRuntimeException = StatusProto.toStatusRuntimeException(newStatus);

                status = Status.fromThrowable(statusRuntimeException);
                headers = statusRuntimeException.getTrailers();
            }

            serverCall.close(status, headers);
        }


        private ErrorInfo fillNewErrorInfo(
                Map<String, String> metadata,
                GrpcError grpcError
        ) {

            return
                    ErrorInfo.newBuilder()
                            .setReason(
                                    grpcError
                                            .name()
                            )
                            .setDomain(
                                    String.format("ID: %s | KEY: %s",
                                            instanceConfig.getId(),
                                            instanceConfig.getKey()
                                    )
                            )
                            .putAllMetadata(metadata)
                            .build();

        }

        private Map<String, String> fillMetaData(
                GrpcError error,
                Exception exception,
                ServerCall<T, R> serverCall) {

            Map<String, String> map = new LinkedHashMap<>();

            map.put(
                    EMetadataKey.STATUS.name(),
                    error
                            .getGrpcStatus()
                            .getCode()
                            .name()
            );
            map.put(
                    EMetadataKey.DESCRIPTION.name(),
                    exception.getMessage() != null
                            && !exception.getMessage().endsWith(": ")
                            ? exception.getMessage()
                            : ""
            );

            map.put(EMetadataKey.METHOD_NAME.name(),
                    serverCall
                            .getMethodDescriptor()
                            .getFullMethodName()
            );
            map.put(
                    EMetadataKey.TIME.name(),
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    DatePatterns.DATE_TIME
                                            )
                            )
            );
            map.put(EMetadataKey.STACKTRACE.name(),
                    convertStackTraceToReadable(
                            exception.getStackTrace()
                    )
            );

            return map;
        }

        private String convertStackTraceToReadable(StackTraceElement[] stackTraceElements) {
            final Map<String, String> data = new LinkedHashMap<>();
            for (int i = 0; i < stackTraceElements.length; i++) {
                data.put(
                        String.valueOf(i),
                        stackTraceElements[i].toString()
                );
            }

            return data.toString();
        }
    }
}
