package com.ojo.mullyuojo.company.application.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 업체가 존재하지 않습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다. 다시 입력해주세요."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public HttpStatus getStatus() {
        return status;}
    public String getMessage() {
        return message;}

}
