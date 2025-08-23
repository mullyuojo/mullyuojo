package com.ojo.mullyuojo.company.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
<<<<<<< HEAD
=======
import com.ojo.mullyuojo.company.domain.CompanyManager;
import com.ojo.mullyuojo.company.domain.CompanyProduct;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
<<<<<<< HEAD

import java.time.LocalDateTime;
=======
import org.apache.catalina.Manager;

import java.time.LocalDateTime;
import java.util.List;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139

@Getter
@Setter
@AllArgsConstructor
public class CompanySearchDto {

    private Long id;
<<<<<<< HEAD
    private Long companyId;
    private Long hubId;
    private Long productId;
=======
    private Long hubId;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
    private CompanyType type;
    private String name;
    private String address;
    private String writer;

<<<<<<< HEAD
=======
    private Long productId;   // <- CompanyProduct ID 리스트
    private Long managerId;

>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
}
