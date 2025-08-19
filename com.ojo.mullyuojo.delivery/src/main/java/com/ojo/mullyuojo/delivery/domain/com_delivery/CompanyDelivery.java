package com.ojo.mullyuojo.delivery.domain.com_delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.status.CompanyDeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.delivery.Delivery;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Data
@Entity(name = "com_delivery_tb")
@NoArgsConstructor
@Table
public class CompanyDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long deliveryId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyDeliveryStatus status;

    @Column(nullable = false)
    private Long originHubId;

    @Column(nullable = false)
    private Long destinationCompanyId;

    @Column(nullable = false)
    private Double estimatedDistance;

    @Column(nullable = false)
    private Double estimatedTime;

    @Column
    private Double actualDistance;

    @Column
    private Double actualTime;

    @Column(nullable = false)
    private Long companyDeliveryManagerId;

    private LocalDateTime departureTime;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    public void softDelete(Long userId){
        this.deletedBy = userId;
    }
    public void changeStatus(CompanyDeliveryStatus status){
        this.status = status;
    }

    public void update(Delivery delivery) {
        this.deliveryId = delivery.getId();
        this.originHubId = delivery.getOriginHubId();
        this.destinationCompanyId = delivery.getDestinationCompanyId();
        this.companyDeliveryManagerId = delivery.getCompanyDeliveryManagerId();
    }

    public CompanyDelivery(Long deliveryId, CompanyDeliveryStatus status, Long originHubId, Long destinationCompanyId, Double estimatedDistance, Double estimatedTime, Long companyDeliveryManagerId) {
        this.deliveryId = deliveryId;
        this.status = status;
        this.originHubId = originHubId;
        this.destinationCompanyId = destinationCompanyId;
        this.estimatedDistance = estimatedDistance;
        this.estimatedTime = estimatedTime;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }
}
