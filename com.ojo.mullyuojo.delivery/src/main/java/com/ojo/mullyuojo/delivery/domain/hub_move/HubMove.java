package com.ojo.mullyuojo.delivery.domain.hub_move;


import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveRequestDto;
import com.ojo.mullyuojo.delivery.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@Entity(name = "hub_move_tb")
@NoArgsConstructor
@Table
public class HubMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long originHubId;

    @Column(nullable = false)
    private Long destinationHubId;

    @Column(nullable = false)
    private Double time;

    @Column(nullable = false)
    private Double distance;

    @Convert(converter = StringListConverter.class)
    private List<Long> managerList ;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    public void softDelete(){
        this.deletedBy = 1L;
    }

    public void addManager(Long managerId){
        this.managerList.add(managerId);
    }

    public void update(HubMoveRequestDto requestDto) {
        this.originHubId = requestDto.originHubId();
        this.destinationHubId = requestDto.destinationHubId();
        this.time = requestDto.time();
        this.distance = requestDto.distance();
    }
    public HubMove(Long originHubId, Long destinationHubId, Double time, Double distance) {
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.time = time;
        this.distance = distance;
        this.managerList = new ArrayList<>();
    }


}
