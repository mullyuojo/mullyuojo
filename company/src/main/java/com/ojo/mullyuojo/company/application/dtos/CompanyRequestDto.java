package com.ojo.mullyuojo.company.application.dtos;


import com.ojo.mullyuojo.company.application.dtos.manager.CompanyManagerDto;
import com.ojo.mullyuojo.company.application.dtos.product.CompanyProductDto;
import com.ojo.mullyuojo.company.domain.CompanyManager;
import com.ojo.mullyuojo.company.domain.CompanyProduct;
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class CompanyRequestDto {

    private Long hubId;
    private CompanyType type;
    private String name;
    private String address;

    private List<CompanyProductDto> products;
    private List<CompanyManagerDto> managers;

}
