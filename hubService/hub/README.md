# Hub 서비스 README

## HubController

REST API 엔드포인트를 제공하며 HubService와 연동합니다.

### 엔드포인트

- **GET /hubs**
    - 설명: 검색 조건과 페이징 기반 허브 목록 조회
    - 파라미터:
        - `HubSearchDto` (검색 조건)
        - `Pageable` (페이지 번호, 사이즈, 정렬)

- **GET /hubs/{id}**
    - 설명: 단일 허브 조회
    - 파라미터: `id` (허브 ID)

- **POST /hubs**
    - 설명: 허브 생성 (권한 검증 포함)
    - 파라미터: `HubRequestDto` (생성 정보), `AccessContext` (사용자 권한)

- **PATCH /hubs/{id}**
    - 설명: 허브 수정 (권한 검증 포함)
    - 파라미터: `id`, `HubRequestDto` (수정 정보), `AccessContext` (사용자 권한)

- **DELETE /hubs/{id}**
    - 설명: 허브 삭제 (Soft delete, 권한 검증 포함)
    - 파라미터: `id`, `AccessContext` (사용자 권한)

---

## HubService

허브 도메인 관련 핵심 비즈니스 로직 수행

- `getHubs(dto, pageable)`  
  검색 조건 및 페이징 기반 허브 목록 조회

- `getHubById(id)`  
  단일 허브 조회

- `createHub(dto, userId, ctx)`  
  허브 생성, 권한 검증 포함, Naver Geocode API를 통한 좌표 변환

- `updateHub(id, dto, ctx)`  
  허브 수정, 권한 검증 포함, 주소 변경 시 좌표 업데이트

- `deleteHub(id, ctx)`  
  Soft delete 처리, 권한 검증 포함

---

## 검색 및 페이징

- `HubSearchDto` 기반 검색 가능:
    - `hubName` (부분검색, 대소문자 무시)
    - `address` (부분검색, 대소문자 무시)
    - `province` (정확히 일치)

- `Pageable`을 통해 페이지 번호, 사이즈, 정렬 가능
- QueryDSL `OrderSpecifier`를 활용하여 동적 정렬 지원
    - 정렬 가능 필드: `createdAt`, `hubName`, `province`

---

## HubRepository / HubRepositoryImpl

- QueryDSL 기반 검색 구현
- BooleanExpression 사용하여 조건별 검색
- 페이징 및 정렬 지원
- `Page<HubResponseDto>` 반환

---

## 권한 검증

- `AccessContext`: 사용자 ID, Role, CompanyId, HubId 포함
- `AccessGuard`: Action 기반 권한 검증 (CREATE, UPDATE, DELETE)

---

## Hub 엔티티

- 필드:
    - `id`, `hubName`, `address`, `province`, `latitude`, `longitude`, `writer`
    - 생성/수정/삭제 시간, 삭제자 정보 (`deletedAt`, `deletedBy`) 포함
- **Soft delete 전략** 사용: 실제 데이터는 삭제되지 않음
- **낙관적 락(@Version)** 적용으로 동시성 충돌 방지
- **Validation**
    - 허브명, 주소, province 값 필수
    - 유효성 검사 실패 시 `BusinessException` 발생
- 좌표 처리:
    - 생성/수정 시 Geocode API 호출하여 `latitude`와 `longitude` 저장

---
# AccessGuard 권한 설정

허브 서비스에서 사용자의 권한(Role)에 따른 액션(Action) 허용 범위를 정의한 표입니다.

---

## 권한 설정 표

| 역할(Role)      | CREATE | READ | UPDATE | DELETE | 설명                     |
|-----------------|--------|------|--------|--------|-------------------------|
| MASTER          | ✅     | ✅   | ✅     | ✅     | 모든 액션 허용          |
| HUB_MANAGER     | ❌     | ✅   | ❌     | ❌     | 조회(READ)만 가능       |
| DELIVERY_USER   | ❌     | ✅   | ❌     | ❌     | 조회(READ)만 가능       |
| COMPANY_STAFF   | ❌     | ✅   | ❌     | ❌     | 조회(READ)만 가능       |

- ✅: 해당 액션 허용
- ❌: 해당 액션 불가 (시도 시 `BusinessException` 발생)

---

## 권한 검증 방식

- `AccessGuard.requiredPermission(Action action, AccessContext ctx)` 메서드를 사용
- 사용자 Role과 수행하려는 Action을 검증
- 인증 정보가 없거나 권한이 없는 경우 `BusinessException` 발생

## 참고 사항

- Soft delete: `deletedAt` 및 `deletedBy` 기록
- 낙관적 락(@Version) 적용
- Validation: 필수 필드 체크 포함
