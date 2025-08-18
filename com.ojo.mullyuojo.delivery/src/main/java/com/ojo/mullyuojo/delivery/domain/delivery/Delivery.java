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
    private Long destinationCompanyId;

    @Column(nullable = false)
    private Long companyManagerId;

    @Column(nullable = false)
    private String companyManagerSlackId;

    @Column(nullable = false)
    private Long hubDeliveryManagerId;

    @Column(nullable = false)
    private Long companyDeliveryManagerId;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    public void changeStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void softDelete(Long userId){
        this.deletedBy = userId;
    }

    public void update( Long orderId, DeliveryStatus status, Long originHubId, Long destinationHubId, Long destinationCompanyId, Long companyManagerId, String companyManagerSlackId, Long hubDeliveryManagerId, Long companyDeliveryManagerId) {
        this.orderId = orderId;
        this.status = status;
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.destinationCompanyId = destinationCompanyId;
        this.companyManagerId = companyManagerId;
        this.companyManagerSlackId = companyManagerSlackId;
        this.hubDeliveryManagerId = hubDeliveryManagerId;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }

    public Delivery( Long orderId, DeliveryStatus status, Long originHubId, Long destinationHubId, Long destinationCompanyId, Long companyManagerId, String companyManagerSlackId, Long hubDeliveryManagerId, Long companyDeliveryManagerId) {
        this.orderId = orderId;
        this.status = status;
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.destinationCompanyId = destinationCompanyId;
        this.companyManagerId = companyManagerId;
        this.companyManagerSlackId = companyManagerSlackId;
        this.hubDeliveryManagerId = hubDeliveryManagerId;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }
}
