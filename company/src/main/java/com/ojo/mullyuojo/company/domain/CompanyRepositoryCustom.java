package com.ojo.mullyuojo.company.domain;

import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<CompanyResponseDto> searchCompanies(CompanySearchDto companySearchDto, Pageable pageable);
}
