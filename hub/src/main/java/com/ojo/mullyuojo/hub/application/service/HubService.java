package com.ojo.mullyuojo.hub.application.service;

import com.ojo.mullyuojo.hub.application.dtos.GeocodeResponse;
import com.ojo.mullyuojo.hub.application.dtos.HubRequestDto;
import com.ojo.mullyuojo.hub.application.dtos.HubResponseDto;
import com.ojo.mullyuojo.hub.application.dtos.HubSearchDto;
import com.ojo.mullyuojo.hub.application.exception.BusinessException;
import com.ojo.mullyuojo.hub.application.exception.ErrorCode;
import com.ojo.mullyuojo.hub.application.security.AccessContext;
import com.ojo.mullyuojo.hub.application.security.AccessGuard;
import com.ojo.mullyuojo.hub.application.security.Action;
import com.ojo.mullyuojo.hub.domain.Hub;
import com.ojo.mullyuojo.hub.domain.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HubService {
    private final GeoService geoService;
    private final HubRepository hubRepository;

    public Page<HubResponseDto> getHubs (HubSearchDto dto, Pageable pageable) {
        return hubRepository.searchHubs(dto, pageable);
    }

    @Transactional(readOnly = true)
    public HubResponseDto getHubById(Long id) {
        Hub hub = hubRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.HUB_NOT_FOUND, "찾으시는 허브가 존재하지 않습니다"));

        return HubResponseDto.from(hub);
    }

    @Transactional
    public HubResponseDto createHub(HubRequestDto dto, String userId, AccessContext ctx) {

        AccessGuard.requiredPermission(Action.CREATE, ctx);

        // 네이버 API 호출
        GeocodeResponse response = geoService.getGeocodeResponse(dto.getAddress());
        GeocodeResponse.Address first = response.getAddresses().get(0);

        BigDecimal latitude = new BigDecimal(first.getY());
        BigDecimal longitude = new BigDecimal(first.getX());

        // 엔티티는 좌표까지 DTO에서 받는 것처럼 처리
        Hub hub = Hub.createHub(dto, userId, ctx, latitude, longitude);
        hub = hubRepository.save(hub);

        return HubResponseDto.from(hub);
    }

    @Transactional
    public HubResponseDto updateHub(Long id, HubRequestDto dto, AccessContext ctx) {
        Hub hub = hubRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND, "찾으시는 허브가 존재하지 않습니다. "));
        AccessGuard.requiredPermission(Action.UPDATE, ctx);

        BigDecimal lat = hub.getLatitude();
        BigDecimal lon = hub.getLongitude();

        if(dto.getAddress() != null && !dto.getAddress().isBlank()) {
            GeocodeResponse response = geoService.getGeocodeResponse(dto.getAddress());
            GeocodeResponse.Address first = response.getAddresses().get(0);
            lat = new BigDecimal(first.getY());
            lon = new BigDecimal(first.getX());
        }

        hub.updateHub(dto, lat, lon);
        hub = hubRepository.save(hub);

        return HubResponseDto.from(hub);
    }

    @Transactional
    public void deleteHub(Long id, AccessContext ctx) {
        Hub hub = hubRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND,"찾으시는 허브가 존재하지 않습니다."));

        AccessGuard.requiredPermission(Action.DELETE, ctx);

        hub.deleteHub(ctx.getUserId());
        hubRepository.save(hub);
    }

}
