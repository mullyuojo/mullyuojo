<<<<<<< HEAD
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
=======
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
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
