package com.ojo.mullyuojo.delivery.domain.delivery_user;

import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserRequestDto;
import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserResponseDto;
import com.ojo.mullyuojo.delivery.domain.delivery_user.dto.CompanyDeliveryUserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyDeliveryUserService {

    private final CompanyDeliveryUserRepository companyDeliveryUserRepository;

    @Transactional
    public CompanyDeliveryUserResponseDto getUser(Long deliveryUserId) {
        CompanyDeliveryUser user = findById(deliveryUserId);
        return CompanyDeliveryUserResponseDto.from(user);
    }

    @Transactional
    public List<CompanyDeliveryUserResponseDto> getAllUser() {
        List<CompanyDeliveryUser> users = companyDeliveryUserRepository.findAllByDeletedByIsNull();
        return users.stream().map(CompanyDeliveryUserResponseDto::from).toList();
    }

    @Transactional
    public CompanyDeliveryUserResponseDto create(CompanyDeliveryUserRequestDto requestDto) {

        Long num = findLastSequenceNum(requestDto.hubId());

        if (num.equals(10L)) {
            throw new RuntimeException("허브의 배송 담당자는 10명을 초과할 수 없습니다.");
        }

        CompanyDeliveryUser user = new CompanyDeliveryUser(
                requestDto.hubId(),
                num + 1L,
                requestDto.userId()
        );

        companyDeliveryUserRepository.save(user);
        return CompanyDeliveryUserResponseDto.from(user);
    }

    @Transactional
    public CompanyDeliveryUserResponseDto update(Long id, CompanyDeliveryUserUpdateRequestDto requestDto) {
        CompanyDeliveryUser user = findById(id);
        user.update(requestDto);
        return CompanyDeliveryUserResponseDto.from(user);
    }

    @Transactional
    public void delete(Long deliveryUserId) {
        CompanyDeliveryUser user = findById(deliveryUserId);
        user.softDelete(1L);
    }

    @Transactional
    public Long findNextUserInHub(Long hubId) {

        Long lastSequence;
        CompanyDeliveryUser lastUser = companyDeliveryUserRepository.findUserOnDelivery(hubId).orElse(null);
        if (lastUser == null) {
            lastSequence = 0L;
        } else {
            lastSequence = lastUser.getSequence();
            if (lastSequence == 10L) { //무조건 10명이 존재한다는 가정
                lastSequence = 0L;
            }
            lastUser.onDelivery(false);
        }
        log.info("마지막 배송 순서 : {}", lastSequence);
        CompanyDeliveryUser nextUser = companyDeliveryUserRepository.findNextUserByHubIdAndLastSequence(hubId, lastSequence);
        nextUser.onDelivery(true);

        return nextUser.getUserId();
    }


    private CompanyDeliveryUser findById(Long id) {
        return companyDeliveryUserRepository.findByIdAndDeletedByIsNull(id).orElseThrow(() -> new RuntimeException("해당 배송 담당자를 찾을 수 없습니다."));
    }

    private Long findLastSequenceNum(Long hubId) {
        Long num = companyDeliveryUserRepository.findLastSequenceNumberByHubId(hubId);
        if (num == null) {
            return 0L;
        }
        return num;
    }


}
