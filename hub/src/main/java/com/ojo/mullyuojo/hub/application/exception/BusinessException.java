package com.ojo.mullyuojo.hub.application.exception;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public static BusinessException forbiddenManagerOnly(){
        return new BusinessException(ErrorCode.ACCESS_DENIED);
    }
    public static BusinessException forbiddenManager(String details){
        return new BusinessException(ErrorCode.ACCESS_DENIED, details);
    }
}
