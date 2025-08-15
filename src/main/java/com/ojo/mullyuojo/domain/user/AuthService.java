package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.domain.user.dto.AuthRequestDto;
import com.ojo.mullyuojo.domain.user.dto.AuthResponseDto;
import com.ojo.mullyuojo.domain.user.dto.UserRequestDto;
import com.ojo.mullyuojo.domain.user.dto.UserResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.ojo.mullyuojo.exception.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public AuthResponseDto signUp (AuthRequestDto authRequestDto) {
        checkUsername(authRequestDto);
        User user = new User(authRequestDto);
        userRepository.save(user);
        return new AuthResponseDto("회원가입이 완료되었습니다");
    }

    private void checkUsername(AuthRequestDto authRequestDto){
        if (userRepository.findByUsername(authRequestDto.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("이미 등록된 사용자가 있습니다. : " + authRequestDto.getUsername());
        }
    }
}

