package com.ojo.mullyuojo.company.application.service;

import com.ojo.mullyuojo.company.application.dtos.CompanyRequestDto;
import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import com.ojo.mullyuojo.company.application.exception.BusinessException;
import com.ojo.mullyuojo.company.application.exception.ErrorCode;
import com.ojo.mullyuojo.company.application.security.AccessContext;
import com.ojo.mullyuojo.company.application.security.AccessGuard;
import com.ojo.mullyuojo.company.application.security.Action;
import com.ojo.mullyuojo.company.application.security.ResourceScope;
import com.ojo.mullyuojo.company.domain.Company;
import com.ojo.mullyuojo.company.domain.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    // 목록 조회
    public Page<CompanyResponseDto> getCompanies(CompanySearchDto searchDto, Pageable pageable) {
        return companyRepository.searchCompanies(searchDto, pageable);
    }
    // 단일 조회
    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.COMPANY_NOT_FOUND));
        return CompanyResponseDto.from(company);
    }

    // 업체 생성
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto dto, String userId, AccessContext ctx) {
        AccessGuard.requiredPermission(
                Action.CREATE,
                ctx,
                ResourceScope.of(dto.getHubId(), dto.getCompanyId())
                );

        Company company = Company.createCompany(dto, userId);

        Company savedCompany = companyRepository.save(company);
        return savedCompany.toResponseDto();
    }

    // 업체 수정
    @Transactional
    public CompanyResponseDto updateCompany(Long id, CompanyRequestDto dto, AccessContext ctx) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.COMPANY_NOT_FOUND));
        AccessGuard.requiredPermission(
                Action.UPDATE,
                ctx,
                ResourceScope.of(company.getHubId(), company.getCompanyId()));

        company.updateCompany(dto);
        return company.toResponseDto();
    }

    // 업체 삭제
    @Transactional
    public void deleteCompany(Long id,String userId, AccessContext ctx) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.COMPANY_NOT_FOUND));
        AccessGuard.requiredPermission(
                Action.DELETE,
                ctx,
                ResourceScope.of(company.getHubId(), company.getCompanyId()));

        company.deleteCompany(userId);
    }


}
