package com.ojo.mullyuojo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    public static <T> ApiResponse<T> success(int status,T data) {
        return new ApiResponse<>(status,null,data);
    }
    public static <T> ApiResponse<T> success(int status, String message) {
        return new ApiResponse<>(status,message,null);
    }
    public static <T> ApiResponse<T> error(int status, String message){
        return new ApiResponse<>(status, message, null);
    }
}