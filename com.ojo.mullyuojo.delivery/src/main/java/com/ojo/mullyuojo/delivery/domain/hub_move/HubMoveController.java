package com.ojo.mullyuojo.delivery.domain.hub_move;

import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveManagerRequestDto;
import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveRequestDto;
import com.ojo.mullyuojo.delivery.domain.hub_move.dto.HubMoveResponseDto;
import com.ojo.mullyuojo.delivery.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hub-infos")
public class HubMoveController {

    private final HubMoveService hubMoveService;

    @GetMapping()
    public ApiResponse<?> getAllHubInfo(){
        List<HubMoveResponseDto> response = hubMoveService.getAllHubInfo();
        return ApiResponse.success(200,response);
    }

    @GetMapping("/{hubMoveId}")
    public ApiResponse<?> getHubInfo(@PathVariable(name = "hubMoveId") Long hubMoveId){
        HubMoveResponseDto response = hubMoveService.getHubInfo(hubMoveId);
        return ApiResponse.success(200,response);
    }

    @PatchMapping("/{hubMoveId}")
    public ApiResponse<?> update(@RequestBody @Valid HubMoveRequestDto requestDto, @PathVariable(name = "hubMoveId") Long hubMoveId){
        hubMoveService.update(requestDto, hubMoveId);
        return ApiResponse.success(200,"허브간 이동 정보 수정 완료");
    }

    @PostMapping()
    public ApiResponse<?> create(@RequestBody @Valid HubMoveRequestDto requestDto){
        HubMoveResponseDto response = hubMoveService.create(requestDto);
        return ApiResponse.success(201,"허브간 이동 정보 생성 완료", response);
    }

    @PostMapping("/{hubMoveId}")
    public ApiResponse<?> addManager(@RequestBody @Valid HubMoveManagerRequestDto requestDto, @PathVariable(name = "hubMoveId") Long hubMoveId){
        HubMoveResponseDto response = hubMoveService.addManager(requestDto, hubMoveId);
        return ApiResponse.success(201,"배송 담당자 등록 완료", response);
    }

    @DeleteMapping("/{hubMoveId}")
    public ApiResponse<?> delete(@PathVariable(name = "hubMoveId") Long hubMoveId){
        hubMoveService.delete(hubMoveId);
        return ApiResponse.success(204,"허브간 이동 정보 삭제 완료");
    }

}
