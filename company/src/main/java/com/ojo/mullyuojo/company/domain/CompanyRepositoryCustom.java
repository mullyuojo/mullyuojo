package com.ojo.mullyuojo.company.domain;

import com.ojo.mullyuojo.company.application.dtos.CompanyResponseDto;
import com.ojo.mullyuojo.company.application.dtos.CompanySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
<<<<<<< HEAD
    Page<CompanyResponseDto> searchCompanies(CompanySearchDto companySearchDto, Pageable pageable);
=======
    Page<Company> searchCompanies(CompanySearchDto companySearchDto, Pageable pageable);
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
}
