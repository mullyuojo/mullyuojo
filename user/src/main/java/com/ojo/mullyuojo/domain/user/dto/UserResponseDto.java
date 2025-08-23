package com.ojo.mullyuojo.domain.user.dto;

import com.ojo.mullyuojo.domain.user.enums.UserRole;
import com.ojo.mullyuojo.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    Long id;
    String username;
    String password;
    String slack_id;
    LocalDateTime deketed_at;
    boolean deleted_by;
    UserRole.UserRoles role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.slack_id = user.getSlack_id();
        this.role = user.getRole();
    }
}
