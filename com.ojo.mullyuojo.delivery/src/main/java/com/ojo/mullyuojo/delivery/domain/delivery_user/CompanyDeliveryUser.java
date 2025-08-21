package com.ojo.mullyuojo.delivery.domain.delivery_user;

import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Data
@Entity(name = "com_delivery_user_tb")
@NoArgsConstructor
@Table
public class CompanyDeliveryUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hubId;

    @Column(nullable = false)
    private Long sequence;

    @Column(nullable = false)
    private Long userId;

    private Boolean onDelivery = false;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    public void softDelete(Long id) {
        this.deletedBy = id;
    }

    public void onDelivery(Boolean b) {
        this.onDelivery = b;
    }

    public void update(CompanyDeliveryUserUpdateRequestDto requestDto) {
        this.hubId = requestDto.hubId();
        this.userId = requestDto.userId();
    }

    public CompanyDeliveryUser(Long hubId, Long sequence, Long userId) {
        this.hubId = hubId;
        this.sequence = sequence;
        this.userId = userId;
    }

}
