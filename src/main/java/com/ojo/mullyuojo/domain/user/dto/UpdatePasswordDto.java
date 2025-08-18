package com.ojo.mullyuojo.domain.user.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordDto {
    String oldPassword;
    String newPassword;
}
