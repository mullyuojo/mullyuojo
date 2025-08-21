package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.domain.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id));
    }

    private User findUser(long user_id) {
        return userRepository.findById(user_id).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + user_id));
    }

    @PreAuthorize("authentication.principal == #user_id.toString() or hasRole('MASTER')")
    public UserResponseDto getUserById(long user_id) {
        User user = findUser(user_id);
        return new UserResponseDto(user);

    }
    @PreAuthorize("hasRole('MASTER')")
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("authentication.principal == #user_id.toString() or hasRole('MASTER')")
    public MessageResponseDto updatePassword(Long user_id, UpdatePasswordDto updatePasswordDto) {
        User user = findUser(user_id);

        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 일치하지 않습니다");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));

        return new MessageResponseDto("비밀번호 변경이 완료되었습니다.");
    }


    @Transactional
    @PreAuthorize("hasRole('MASTER')")
    public MessageResponseDto updateUser(long userId, UpdateUserDto updateUserDto) {
        User user = findUser(userId);
        
        // User 엔티티에 업데이트 로직 위임
        user.update(updateUserDto, passwordEncoder);
        
        return new MessageResponseDto("유저 정보 변경이 완료되었습니다.");
    }

    @Transactional
    @PreAuthorize("hasRole('MASTER')")
    public MessageResponseDto deleteUser(long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User deleter =  (User) authentication.getPrincipal();
        Long deleter_id = deleter.getId();

        User user = findUser(userId);
        user.setDeleted_by(deleter_id);
        user.setDeleted_at(LocalDateTime.now());

        return new MessageResponseDto("유저 삭제가 완료되었습니다.");
    }
}
