package com.ojo.mullyuojo.delivery.domain.delivery;

import com.ojo.mullyuojo.delivery.domain.delivery.status.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Data
@Entity(name = "delivery_tb")
@NoArgsConstructor
@Table
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(nullable = false)
    private Long originHubId;

    @Column(nullable = false)
    private Long destinationHubId;

    @Column(nullable = false)
    private String companyAddress;

    @Column(nullable = false)
    private Long companyManagerId;

    @Column(nullable = false)
    private String companyManagerSlackId;

    @Column(nullable = false)
    private Long CompanyDeliveryManagerId;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    public void changeStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void softDelete(Long userId){
        this.deletedBy = userId;
    }

    public Delivery( Long orderId, DeliveryStatus status, Long originHubId, Long destinationHubId, String companyAddress, Long companyManagerId, String companyManagerSlackId, Long companyDeliveryManagerId) {
        this.orderId = orderId;
        this.status = status;
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.companyAddress = companyAddress;
        this.companyManagerId = companyManagerId;
        this.companyManagerSlackId = companyManagerSlackId;
        CompanyDeliveryManagerId = companyDeliveryManagerId;
    }
}
