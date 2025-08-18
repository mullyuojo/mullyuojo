package com.ojo.mullyuojo.delivery.exception;

import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import jakarta.security.auth.message.AuthException;
//import jakarta.ws.rs.BadRequestException;
//import jakarta.ws.rs.ForbiddenException;
//import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //----------------------------400 Bad request
//    @ExceptionHandler(BadRequestException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiResponse<?> handleBadRequestException(BadRequestException ex) {
//        log.error("Validation error: {}", ex.getMessage());
//        return ApiResponse.error(400, ex.getMessage());
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleAllException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        return ApiResponse.error(400, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // 유효성 검증 ( @Valid )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult()
                .getFieldError()).getDefaultMessage();
        log.error("Validation error: {}", errorMessage);
        return ApiResponse.error(400, errorMessage);
    }

    //----------------------------401 Unauthorized
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleUnauthorizedException(HttpClientErrorException ex) {
        log.error("Unauthorized : {}", ex.getMessage());
        return ApiResponse.error(401, ex.getMessage());
    }
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleAuthException(AuthException ex) {
        log.error("AuthException: {}", ex.getMessage());
        return ApiResponse.error(401, ex.getMessage());
    }

    //----------------------------403 Forbidden
//    @ExceptionHandler(ForbiddenException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ApiResponse<?> handleForbiddenException(ForbiddenException ex) {
//        log.error("ForbiddenException: {}", ex.getMessage());
//        return ApiResponse.error(404, ex.getMessage());
//    }
//
//
//    //----------------------------404 Not found
//    @ExceptionHandler(NotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ApiResponse<?> handleNotFoundException(NotFoundException ex) {
//        log.error("NotFoundException: {}", ex.getMessage());
//        return ApiResponse.error(404, ex.getMessage());
//    }

    //----------------------------500 Internal server error
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException: {}", ex.getMessage());
        return ApiResponse.error(500, "서버 내부 오류입니다.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneralException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage());
        return ApiResponse.error(500, ex.getMessage());
    }
}
