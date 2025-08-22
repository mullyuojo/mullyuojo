package com.ojo.mullyuojo.domain.user.dto;

import com.ojo.mullyuojo.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthRequestDto {

    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z\\d]+$", message = "사용자 이름은 알파벳 소문자와 숫자만 사용가능합니다.")
    String username;

    @NotBlank(message = "비밀 번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$",message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    String password;
    String slack_id;
    UserRole.UserRoles role;
}
