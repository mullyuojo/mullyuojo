package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.domain.user.dto.AuthRequestDto;
import com.ojo.mullyuojo.domain.user.dto.UserRequestDto;
import com.ojo.mullyuojo.domain.user.enums.UserRole.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String slack_id;
    LocalDateTime deketed_at;
    boolean deleted_by;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRoles role;

    public User(AuthRequestDto authRequestDto) {
        this.username = authRequestDto.getUsername();
        this.password = authRequestDto.getPassword();
        this.slack_id = authRequestDto.getSlack_id();
        this.role = authRequestDto.getRole();
    }


}
