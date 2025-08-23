package com.ojo.mullyuojo.hub.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DeliveryManagerList", indexes = { @Index(name = "idx_deliveryManagerList_name", columnList = "name") })
public class DeliveryManagerList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryManager_id")
    private Long id;

    // Company ↔ Hub N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private Hub hub;

    private String name;


}
