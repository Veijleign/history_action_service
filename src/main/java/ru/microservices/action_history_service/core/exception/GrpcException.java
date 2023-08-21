package ru.microservices.action_history_service.core.exception;

import lombok.Getter;

@Getter
public class GrpcException extends RuntimeException {
    private final GrpcError error;


    public GrpcException(GrpcError error) {
        super();
        this.error = error;
    }

    public GrpcException(String message, GrpcError error) {
        super(message);
        this.error = error;
    }

    public static GrpcException of(GrpcError error) {
        return new GrpcException(error);
    }

    public static GrpcException of(Throwable cause) {
        return cause instanceof GrpcException
                ? (GrpcException) cause
                : new GrpcException(
                cause.getMessage(),
                GrpcError.INTERNAL
        );
    }
}
