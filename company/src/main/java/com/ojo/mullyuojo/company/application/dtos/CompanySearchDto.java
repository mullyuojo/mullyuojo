package com.ojo.mullyuojo.company.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ojo.mullyuojo.company.domain.CompanyManager;
import com.ojo.mullyuojo.company.domain.CompanyProduct;
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.Manager;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CompanySearchDto {

    private Long id;
    private Long hubId;
    private CompanyType type;
    private String name;
    private String address;
    private String writer;

    private Long productId;   // <- CompanyProduct ID 리스트
    private Long managerId;

}
