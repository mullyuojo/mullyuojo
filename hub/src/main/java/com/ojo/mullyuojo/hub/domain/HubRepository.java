package com.ojo.mullyuojo.hub.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HubRepository extends JpaRepository<Hub, Long>, HubRepositoryCustom {
    Optional<Hub> findByIdAndDeletedAtIsNull(Long id);
}
