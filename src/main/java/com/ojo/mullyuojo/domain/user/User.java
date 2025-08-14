package com.ojo.mullyuojo.domain.user;

import com.ojo.mullyuojo.domain.user.enums.UserRole.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
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
}
