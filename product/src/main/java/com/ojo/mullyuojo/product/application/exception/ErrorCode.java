package com.ojo.mullyuojo.product.application.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 제품이 존재하지 않습니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "삭제 가능한 수량을 초과하였습니다. 다시 입력해주세요"),
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
