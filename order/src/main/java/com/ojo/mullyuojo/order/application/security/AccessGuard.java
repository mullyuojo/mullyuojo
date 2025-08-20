package com.ojo.mullyuojo.order.application.security;

import com.ojo.mullyuojo.order.application.exception.BusinessException;
import com.ojo.mullyuojo.order.application.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessGuard {
    public static void requiredPermission(Action action, AccessContext ctx, ResourceScope scope) {
        if (ctx == null || ctx.getRole() == null){
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "인증정보가 없습니다");
        }
        switch(ctx.getRole()){
            case MASTER -> {
                return;
            }
            case HUB_MANAGER -> {
                requireHub(scope);
                if(ctx.getHubId() == null || ! ctx.getHubId().equals(scope.getHubId())){
                    throw new BusinessException(ErrorCode.ACCESS_DENIED, "허브 관리자 권한은 담당 허브에서만 가능합니다.");
                }
                return;
            }
            case DELIVERY_USER -> {
                requireHub(scope);
                if(!ctx.getHubId().equals(scope.getHubId())){
                    throw new BusinessException(ErrorCode.ACCESS_DENIED, "담당 허브를 벗어난 조회입니다.");
                }
                if(!(action == Action.READ)){
                    throw new BusinessException(ErrorCode.ACCESS_DENIED, "배송 담당자는 조회만 가능합니다.");
                }
                return;
            }
            case COMPANY_STAFF -> {
                requireCompany(scope);
                if(!ctx.getCompanyId().equals(scope.getSupplierId()) && !ctx.getCompanyId().equals(scope.getReceiverId())){
                    if(action == Action.CREATE || action == Action.UPDATE){
                        throw new BusinessException(ErrorCode.ACCESS_DENIED, "담당 업체의 주문 생성 또는 수정만 가능합니다");
                    }
                }
                if(action == Action.DELETE){
                    throw new BusinessException(ErrorCode.ACCESS_DENIED, "업체 담당자는 삭제 권한이 없습니다.");

                }
            }
        }

    }
    private static void requireHub(ResourceScope scope){
        if(scope == null || scope.getHubId() == null){
            throw new BusinessException(ErrorCode.INVALID_INPUT, "담당 허브아이디가 필요합니다.");
        }
    }
    private static void requireCompany(ResourceScope scope){
        if (scope == null || scope.getSupplierId() == null || scope.getReceiverId() == null){
            throw new BusinessException(ErrorCode.INVALID_INPUT, "담당 업체아이디가 필요합니다.");
        }
    }

    private static void requireTrue(boolean expr, String msg) {
        if (!expr) throw new BusinessException(ErrorCode.ACCESS_DENIED, msg);
    }
}
