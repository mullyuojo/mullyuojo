package com.ojo.mullyuojo.delivery.domain.hub_move;

import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveManagerRequestDto;
import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveRequestDto;
import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubMoveService {

    private final HubMoveRepository hubMoveRepository;

    @Transactional
    public List<Double> estimate(Long originHubId, Long destinationHubId) {
        HubMove hubMove = hubMoveRepository.findByOriginHubIdAndDestinationHubIdAndDeletedByIsNull(originHubId, destinationHubId);
        List<Double> result = new ArrayList<>();
        result.add(hubMove.getDistance());
        result.add(hubMove.getTime());
        return result;
    }
    @Transactional
    public HubMoveResponseDto getHubInfo(Long hubMoveId) {
        HubMove hubMove = findById(hubMoveId);
        return HubMoveResponseDto.from(hubMove);
    }

    @Transactional
    public List<HubMoveResponseDto> getAllHubInfo() {
        List<HubMove> hubMoves = hubMoveRepository.findAllAndDeletedByIsNull();
        return hubMoves.stream().map(HubMoveResponseDto::from).collect(Collectors.toList());
    }

    @Transactional
    public HubMoveResponseDto create(HubMoveRequestDto requestDto) {
        HubMove hubMove = new HubMove(
                requestDto.originHubId(),
                requestDto.destinationHubId(),
                requestDto.time(),
                requestDto.distance()
        );
        hubMoveRepository.save(hubMove);
        return HubMoveResponseDto.from(hubMove);
    }

    @Transactional
    public void update(HubMoveRequestDto requestDto, Long hubMoveId) {
        HubMove hubMove = findById(hubMoveId);
        hubMove.update(requestDto);
    }

    @Transactional
    public HubMoveResponseDto addManager(HubMoveManagerRequestDto requestDto, Long hubMoveId) {
        HubMove hubMove = findById(hubMoveId);
        for ( Long id: requestDto.managerList()
             ) {
            if (hubMove.getManagerList().contains(id)){
                throw new RuntimeException("이미 등록이 되어있습니다.");
            }
            hubMove.addManager(id);
        }
        return HubMoveResponseDto.from(hubMove);
    }

    public void delete(Long hubMoveId) {
        HubMove hubMove = findById(hubMoveId);
        hubMove.softDelete();
    }

    private HubMove findById(Long id) {
        return hubMoveRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 허브 이동을 찾을 수 없습니다."));
    }


}
