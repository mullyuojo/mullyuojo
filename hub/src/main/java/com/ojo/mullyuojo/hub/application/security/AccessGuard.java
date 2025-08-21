package com.ojo.mullyuojo.hub.application.security;

import com.ojo.mullyuojo.hub.application.exception.BusinessException;
import com.ojo.mullyuojo.hub.application.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessGuard {
    public static void requiredPermission(Action action, AccessContext ctx) {
        if (ctx == null || ctx.getRole() == null){
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "인증정보가 없습니다");
        }
        switch(ctx.getRole()){
            case MASTER -> {
                return;
            }
            case HUB_MANAGER, DELIVERY_USER, COMPANY_STAFF -> {
                // 조회만 허용
                if(action != Action.READ){
                    throw new BusinessException(ErrorCode.ACCESS_DENIED,"조회 및 검색만 가능힙니다.");
                }
                return;
            }
        }

    }
}
