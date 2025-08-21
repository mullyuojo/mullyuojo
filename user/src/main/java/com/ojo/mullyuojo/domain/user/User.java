package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.domain.user.dto.AuthRequestDto;
import com.ojo.mullyuojo.domain.user.dto.UpdateUserDto;
import com.ojo.mullyuojo.domain.user.dto.UserRequestDto;
import com.ojo.mullyuojo.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "p_users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String slack_id;
    LocalDateTime deleted_at;
    Long deleted_by;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole.UserRoles role;

    public User(AuthRequestDto authRequestDto, PasswordEncoder passwordEncoder) {
        this.username = authRequestDto.getUsername();
        this.password = passwordEncoder.encode(authRequestDto.getPassword());
        this.slack_id = authRequestDto.getSlack_id();
        this.role = authRequestDto.getRole();
    }

    public void update(UpdateUserDto requestDto, PasswordEncoder passwordEncoder) {

        if (requestDto.getUsername() != null && !requestDto.getUsername().isBlank()) {
            this.username = requestDto.getUsername();
        }

        if (requestDto.getSlack_id() != null) {
            this.slack_id = requestDto.getSlack_id();
        }

        if (requestDto.getRole() != null) {
            this.role = requestDto.getRole();
        }

        // DTO에 새로운 비밀번호가 존재할 경우에만 암호화하여 변경
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            this.password = passwordEncoder.encode(requestDto.getPassword());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return deleted_at == null;
    }
}
