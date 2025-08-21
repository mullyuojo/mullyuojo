package com.ojo.mullyuojo.hub.application.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessContext {
    private final String userId;
    private final Role role;
    private final Long companyId;
    private final Long hubId;

    public static AccessContext of(String userId, Role role, Long companyId, Long hubId) {
        return new AccessContext(userId, role, companyId, hubId);
    }

}
