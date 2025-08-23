package com.ojo.mullyuojo.company.application.dtos;


<<<<<<< HEAD
=======
import com.ojo.mullyuojo.company.application.dtos.manager.CompanyManagerDto;
import com.ojo.mullyuojo.company.application.dtos.product.CompanyProductDto;
import com.ojo.mullyuojo.company.domain.CompanyManager;
import com.ojo.mullyuojo.company.domain.CompanyProduct;
>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
import com.ojo.mullyuojo.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

<<<<<<< HEAD
=======
import java.util.List;

>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139

@Getter
@Setter
@AllArgsConstructor
public class CompanyRequestDto {
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

<<<<<<< HEAD
=======
    private List<CompanyProductDto> products;
    private List<CompanyManagerDto> managers;

>>>>>>> 92fb4c33cf5f2c9d97d49a95941d52f0216e7139
}
