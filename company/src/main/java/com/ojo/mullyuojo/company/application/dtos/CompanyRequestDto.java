package com.ojo.mullyuojo.company.application.dtos;


import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CompanyRequestDto {
    private Long companyId;
    private Long hubId;
    private Long productId;
    private CompanyType type;
    private String name;
    private String address;

}
