package com.ojo.mullyuojo.company.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_manager", indexes = { @Index(name = "idx_manager_name", columnList = "name") })
public class CompanyManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "slackId", length = 100)
    private String slackId;
}
