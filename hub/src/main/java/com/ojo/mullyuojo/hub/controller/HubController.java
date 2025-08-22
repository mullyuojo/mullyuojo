package com.ojo.mullyuojo.hub.controller;

import com.ojo.mullyuojo.hub.application.dtos.HubRequestDto;
import com.ojo.mullyuojo.hub.application.dtos.HubResponseDto;
import com.ojo.mullyuojo.hub.application.dtos.HubSearchDto;
import com.ojo.mullyuojo.hub.application.security.AccessContext;
import com.ojo.mullyuojo.hub.application.security.Role;
import com.ojo.mullyuojo.hub.application.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("hubs")
public class HubController {

    private final HubService hubService;

    @ModelAttribute
    public AccessContext setAccessContext(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Role") Role role,
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Hub-Id") Long hubId
    ) {
        return new AccessContext(userId, role, companyId, hubId);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<HubResponseDto>> getHubs (
            @RequestBody HubSearchDto dto,
            Pageable pageable) {
        return ResponseEntity.ok(hubService.getHubs(dto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HubResponseDto> getHub (
            @PathVariable Long id){
        return ResponseEntity.ok(hubService.getHubById(id));
    }

    @PostMapping
    public ResponseEntity<HubResponseDto> createHub (@Valid @RequestBody HubRequestDto dto,
                                                     @ModelAttribute AccessContext ctx){
        return ResponseEntity.ok(hubService.createHub(dto, ctx.getUserId(), ctx));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HubResponseDto> updateHub (
            @PathVariable Long id,
            @RequestBody HubRequestDto dto,
            @ModelAttribute AccessContext ctx){
        return ResponseEntity.ok(hubService.updateHub(id, dto, ctx ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHub (@PathVariable Long id,
                                           @ModelAttribute AccessContext ctx){
        hubService.deleteHub(id, ctx);
        return ResponseEntity.noContent().build();
    }
}
