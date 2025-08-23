<<<<<<< HEAD
package com.ojo.mullyuojo.domain.user.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {
    String message;
    String accessToken;
    String refreshToken;

    public AuthResponseDto(String message) {
        this.message = message;
    }
    public AuthResponseDto(String message, String accessToken, String refreshToken) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
=======
package com.ojo.mullyuojo.domain.user.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {
    String message;
    String accessToken;
    String refreshToken;

    public AuthResponseDto(String message) {
        this.message = message;
    }
    public AuthResponseDto(String message, String accessToken, String refreshToken) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
