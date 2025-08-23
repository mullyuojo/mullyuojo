package com.ojo.mullyuojo.product.application.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceScope {
    private final Long hubId;
    private final Long companyId;

    public static ResourceScope of(Long hubId, Long companyId) {
        return new ResourceScope(hubId, companyId);
    }
}
