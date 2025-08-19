package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.domain.user.dto.AuthRequestDto;
import com.ojo.mullyuojo.domain.user.dto.AuthResponseDto;
import com.ojo.mullyuojo.domain.user.dto.UserRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp (@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authService.signUp(authRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn (@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authService.logIn(authRequestDto));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;

    }
}
