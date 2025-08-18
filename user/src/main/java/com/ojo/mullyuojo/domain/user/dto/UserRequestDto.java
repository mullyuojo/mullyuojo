package com.ojo.mullyuojo.domain.user.dto;

import com.ojo.mullyuojo.domain.user.enums.UserRole.UserRoles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class UserRequestDto {
    Long id;
    String username;
    String password;
    String slack_id;
    LocalDateTime deketed_at;
    boolean deleted_by;
    UserRoles role;
}
