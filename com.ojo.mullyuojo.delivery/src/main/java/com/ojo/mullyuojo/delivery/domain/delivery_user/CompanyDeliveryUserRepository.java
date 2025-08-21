package com.ojo.mullyuojo.delivery.domain.delivery_user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDeliveryUserRepository extends JpaRepository<CompanyDeliveryUser, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u.sequence from com_delivery_user_tb u where u.hubId = :hubId and u.deletedBy is null order by u.sequence desc limit 1")
    Long findLastSequenceNumberByHubId(@Param("hubId") Long hubId);

    Optional<CompanyDeliveryUser> findByIdAndDeletedByIsNull(Long id);

    List<CompanyDeliveryUser> findAllByDeletedByIsNull();

    @Query("select u from com_delivery_user_tb u where u.hubId = :hubId and u.onDelivery = true and u.deletedBy is null")
    Optional<CompanyDeliveryUser> findUserOnDelivery(@Param("hubId") Long hubId);

    @Query("select u from com_delivery_user_tb u where u.hubId = :hubId and u.sequence > :lastSequence and u.deletedBy is null order by u.sequence asc limit 1")
    CompanyDeliveryUser findNextUserByHubIdAndLastSequence(@Param("hubId") Long hubId, @Param("lastSequence") Long lastSequence);

    @Query("select u from com_delivery_user_tb u where u.hubId = :hubId and u.deletedBy is null order by u.sequence asc limit 1")
    Optional<CompanyDeliveryUser> findFirstUserInHub(@Param("hubId") Long hubId);
}
