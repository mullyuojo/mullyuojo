package com.ojo.mullyuojo.company.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ojo.mullyuojo.company.application.dtos.manager.CompanyManagerDto;
import com.ojo.mullyuojo.company.application.dtos.product.CompanyProductDto;
import com.ojo.mullyuojo.company.domain.Company;
import com.ojo.mullyuojo.company.domain.CompanyManager;
import com.ojo.mullyuojo.company.domain.CompanyProduct;
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponseDto {

    private Long id;
    private Long hubId;
    private CompanyType type;
    private String name;
    private String address;
    private String writer;

    private List<CompanyProductDto> products;
    private List<CompanyManagerDto> managers;

    public static CompanyResponseDto from(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .hubId(company.getHubId())
                .type(company.getType())
                .name(company.getName())
                .address(company.getAddress())
                .writer(company.getWriter())               // CompanyProduct -> CompanyProductDto 변환
                .products(company.getProducts() != null
                        ? company.getProducts().stream()
                        .map(p -> new CompanyProductDto(p.getId(), p.getName()))
                        .toList()
                        : new ArrayList<>()
                )
                .managers(company.getManagers() != null
                        ? company.getManagers().stream()
                        .map(m -> new CompanyManagerDto(m.getId(), m.getName(), m.getSlackId()))
                        .toList()
                        : new ArrayList<>()
                )
                .build();
    }
}
