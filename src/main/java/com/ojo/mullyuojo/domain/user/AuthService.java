package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.config.JwtTokenProvider;
import com.ojo.mullyuojo.domain.user.dto.AuthRequestDto;
import com.ojo.mullyuojo.domain.user.dto.AuthResponseDto;
import com.ojo.mullyuojo.exception.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDto signUp (AuthRequestDto authRequestDto) {
        checkUsername(authRequestDto);
        User user = new User(authRequestDto, passwordEncoder);
        userRepository.save(user);
        return new AuthResponseDto("회원가입이 완료되었습니다");
    }

    public AuthResponseDto logIn (AuthRequestDto authRequestDto) {
        User user = userRepository.findByUsername(authRequestDto.getUsername()).orElseThrow(() -> new NoSuchElementException("사용자 이름 " + authRequestDto.getUsername() + "을 찾을수 없습니다"));

        if(!passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        Long userId = user.getId();
        String accessToken = jwTokenProvider.createAccessToken(userId.toString());
        String refreshToken = jwTokenProvider.createRefreshToken(userId.toString());

        return new AuthResponseDto("로그인 완료", accessToken, refreshToken);
    }

    private void checkUsername(AuthRequestDto authRequestDto){
        if (userRepository.findByUsername(authRequestDto.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("이미 등록된 사용자가 있습니다. : " + authRequestDto.getUsername());
        }
    }
}

