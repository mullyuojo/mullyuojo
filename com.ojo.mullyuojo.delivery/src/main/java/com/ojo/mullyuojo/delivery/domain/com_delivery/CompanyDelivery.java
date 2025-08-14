package com.ojo.mullyuojo.delivery.domain.com_delivery;

import com.ojo.mullyuojo.delivery.domain.com_delivery.status.CompanyDeliveryStatus;
import com.ojo.mullyuojo.delivery.domain.hub_delivery.status.HubDeliveryStatus;
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
    private Long HubDeliveryManagerId;

    private LocalDateTime departureTime;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    public void softDelete(Long userId){
        this.deletedBy = userId;
    }

    public CompanyDelivery(Long id, Long deliveryId, CompanyDeliveryStatus status, Long originHubId, Long destinationCompanyId, Double estimatedDistance, Double estimatedTime, Double actualDistance, Double actualTime, Long hubDeliveryManagerId) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.status = status;
        this.originHubId = originHubId;
        this.destinationCompanyId = destinationCompanyId;
        this.estimatedDistance = estimatedDistance;
        this.estimatedTime = estimatedTime;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        HubDeliveryManagerId = hubDeliveryManagerId;
    }
}
