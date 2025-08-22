package com.ojo.mullyuojo.company.controller;

import com.ojo.mullyuojo.company.application.dtos.CompanyRequestDto;
import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import com.ojo.mullyuojo.company.application.security.AccessContext;
import com.ojo.mullyuojo.company.application.security.Role;
import com.ojo.mullyuojo.company.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    @ModelAttribute
    public AccessContext setAccessContext(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-Role") Role role,
            @RequestHeader("X-Company-Id") Long companyId,
            @RequestHeader("X-Hub-Id") Long hubId
    ) {
        return new AccessContext(userId, role, companyId, hubId);
    }

    // 목록 조회
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getCompanies(
            @ModelAttribute CompanySearchDto searchDto,
            Pageable pageable) {
        return ResponseEntity.ok(companyService.getCompanies(searchDto, pageable));
    }
    // 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompany(
            @PathVariable Long id){
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    // 업체 생성
    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
            @Valid @RequestBody CompanyRequestDto dto,
            @ModelAttribute AccessContext ctx){
        return ResponseEntity.ok(companyService.createCompany(dto, ctx.getUserId(), ctx));
    }

    // 업체 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequestDto dto,
            @ModelAttribute AccessContext ctx){
        return ResponseEntity.ok(companyService.updateCompany(id, dto, ctx));
    }

    // 업체 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id,
            @ModelAttribute AccessContext ctx){
        companyService.deleteCompany(id, ctx.getUserId(), ctx);
        return ResponseEntity.noContent().build();
    }
}
