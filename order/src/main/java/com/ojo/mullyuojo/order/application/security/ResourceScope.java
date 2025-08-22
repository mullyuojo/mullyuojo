package com.ojo.mullyuojo.order.application.security;

import com.ojo.mullyuojo.order.application.exception.BusinessException;
import com.ojo.mullyuojo.order.application.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceScope {
    private final Long hubId;
    private final Long supplierId;
    private final Long receiverId;

    public static ResourceScope of(Long hubId, Long supplierId, Long receiverId) {
        return new ResourceScope(hubId, supplierId, receiverId);
    }
}
