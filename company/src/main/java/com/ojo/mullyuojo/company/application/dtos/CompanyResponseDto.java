package com.ojo.mullyuojo.company.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojo.mullyuojo.company.domain.Company;
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponseDto {

    Long id;
    private Long companyId;
    private Long hubId;
    private Long productId;
    private CompanyType type;
    private String name;
    private String address;
    private String writer;

    public static CompanyResponseDto from(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .companyId(company.getCompanyId())
                .hubId(company.getHubId())
                .productId(company.getProductId())
                .type(company.getType())
                .name(company.getName())
                .address(company.getAddress())
                .writer(company.getWriter())
                .build();
    }
}
