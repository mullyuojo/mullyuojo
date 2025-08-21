package com.ojo.mullyuojo.product.application.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequestDto {

    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Long hubId;
    private Long companyId;

}
