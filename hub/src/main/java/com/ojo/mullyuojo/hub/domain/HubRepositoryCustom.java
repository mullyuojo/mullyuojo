package com.ojo.mullyuojo.hub.domain;

import com.ojo.mullyuojo.hub.application.dtos.HubResponseDto;
import com.ojo.mullyuojo.hub.application.dtos.HubSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRepositoryCustom {
    Page<Hub> searchHubs(HubSearchDto hubSearchDto, Pageable pageable);
}
