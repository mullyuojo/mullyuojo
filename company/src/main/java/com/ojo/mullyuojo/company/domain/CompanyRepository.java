package com.ojo.mullyuojo.company.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom {
    Optional<Company> findByIdAndDeletedAtIsNull(Long id);
}
