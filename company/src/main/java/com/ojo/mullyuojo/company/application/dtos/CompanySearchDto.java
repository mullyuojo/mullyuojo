package com.ojo.mullyuojo.company.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CompanySearchDto {

    private Long id;
    private Long companyId;
    private Long hubId;
    private Long productId;
    private CompanyType type;
    private String name;
    private String address;
    private String writer;

}
