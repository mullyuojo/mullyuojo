package com.ojo.mullyuojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 유효성 검사 실패 시 발생하는 MethodArgumentNotValidException을 처리합니다.
     * 주로 @Valid 어노테이션이 붙은 DTO의 유효성 검사 실패 시 발생합니다.
     * HTTP 상태 코드 400 Bad Request와 함께 필드별 에러 메시지를 반환합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * 중복된 사용자 이름으로 회원가입 시도 시 발생하는 DuplicateUsernameException을 처리합니다.
     * HTTP 상태 코드 409 Conflict와 함께 에러 메시지를 반환합니다.
     */
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Conflict");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * 리소스를 찾을 수 없을 때 발생하는 NoSuchElementException을 처리합니다.
     * 예를 들어, 존재하지 않는 ID로 데이터를 조회할 때 발생할 수 있습니다.
     * HTTP 상태 코드 404 Not Found와 함께 에러 메시지를 반환합니다.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NoSuchElementException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 예상치 못한 런타임 예외(RuntimeException)를 처리하는 폴백 핸들러입니다.
     * 명시적으로 처리되지 않은 모든 런타임 예외를 잡아냅니다.
     * HTTP 상태 코드 500 Internal Server Error와 함께 에러 메시지를 반환합니다.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage()); // 개발 단계에서는 상세 메시지 포함, 운영에서는 일반적인 메시지 권장
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 모든 예외를 처리하는 최종 폴백 핸들러입니다.
     * 위에서 정의된 어떤 핸들러에도 잡히지 않은 모든 예외를 처리합니다.
     * HTTP 상태 코드 500 Internal Server Error와 함께 일반적인 에러 메시지를 반환합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred."); // 운영 환경에서는 일반적인 메시지 사용
        // ex.printStackTrace(); // 개발/디버깅 목적으로만 사용, 운영 환경에서는 로그로 대체
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}