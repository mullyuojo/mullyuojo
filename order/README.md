# Mullyuojo Backend

Spring Boot 기반의 Mullyuojo 백엔드 애플리케이션으로, **허브(Hub) 관리**와 **주문(Order) 관리** 기능을 제공합니다.  
QueryDSL과 JPA를 활용한 동적 검색, 페이징, 소프트 삭제 및 권한 기반 액세스 컨트롤이 구현되어 있습니다.

---

## 📂 프로젝트 구조

src/main/java/com/ojo/mullyuojo/
├─ hub/ # 허브(Hub) 모듈
│ ├─ controller/ # REST 컨트롤러
│ ├─ service/ # 서비스 레이어
│ ├─ domain/ # 엔티티 및 Repository 구현체
│ └─ application/dtos/ # DTO
├─ order/ # 주문(Order) 모듈
│ ├─ controller/
│ ├─ application/
│ ├─ domain/
│ └─ application/dtos/
└─ application/ # 공통 설정 및 보안 관련 코드


---

## ⚙️ 기술 스택

- Spring Boot 3.x
- Spring Data JPA
- QueryDSL
- Hibernate
- Jakarta Validation
- Lombok
- PostgreSQL (또는 MySQL)
- Auditing: `@CreatedDate`, `@CreatedBy`, `@LastModifiedDate`
- 소프트 삭제: `@SQLDelete`, `@Where`

---

## 🔑 주요 기능

### Hub 모듈

- CRUD: 허브 생성, 조회, 수정, 삭제
- 네이버 지오코딩 API 연동 (`GeoService`)
- 동적 검색 및 페이징 (`HubRepositoryImpl`)
- 소프트 삭제 지원 (`deletedAt`, `deletedBy`)
- 권한 검증: `AccessGuard` + `Action` (CREATE, READ, UPDATE, DELETE)

#### API 예시

| Method | URL         | 설명                |
|--------|------------|-------------------|
| GET    | /hubs      | 허브 목록 조회     |
| GET    | /hubs/{id} | 단일 허브 조회     |
| POST   | /hubs      | 허브 생성          |
| PATCH  | /hubs/{id} | 허브 수정          |
| DELETE | /hubs/{id} | 허브 삭제          |

---

### Order 모듈

- CRUD: 주문 생성, 조회, 취소, 삭제
- 동적 검색 및 페이징 (`OrderRepositoryImpl`)
- 주문 상태 관리: `PENDING`, `PAYMENT_COMPLETE`, `ORDER_CONFIRMED`, `ORDER_CANCELED`, `DELIVERY_COMPLETE`, `REFUND_REQUESTED`, `REFUND_COMPLETE`
- 주문 취소 조건 검증 (`cancelOrder`)
- 소프트 삭제 지원 (`deletedAt`, `deletedBy`)
- 권한 검증: `AccessGuard` + `Action` + `ResourceScope`

#### API 예시

| Method | URL                | 설명                  |
|--------|------------------|---------------------|
| GET    | /orders          | 주문 목록 조회       |
| GET    | /orders/{id}     | 단일 주문 조회       |
| POST   | /orders          | 주문 생성            |
| PATCH  | /orders/{id}/cancel | 주문 취소          |
| DELETE | /orders/{id}     | 주문 삭제            |

---

## 🔒 보안 & 권한

- `AccessContext` : 사용자 정보, 역할, 회사/허브 ID
- `AccessGuard.requiredPermission()` : Action별 권한 검증
- `ResourceScope` : 주문 관련 Hub, Supplier, Receiver 기준 권한 체크

---

## 🛠 개발 가이드

1. **환경 구성**
    - Java 17+
    - Gradle 또는 Maven
    - DB 설정 (`application.yml`에서 datasource 설정)

2. **빌드 및 실행**
```bash
./gradlew clean build
./gradlew bootRun
```
3. **테스트용 API 호출**
- Postman 또는 Swagger 사용 가능
- X-User-Id, X-Role, X-Company-Id, X-Hub-Id 헤더 필요

4. **QueryDSL 사용**
- 동적 조건 처리: BooleanBuilder 또는 BooleanExpression
- 페이징: Pageable + PageImpl
- 정렬: OrderSpecifier (Hub 모듈)

## 📌 엔티티 관계

### Hub
- `id` (PK)
- `hubName`, `address`, `province`
- `latitude`, `longitude`
- `writer`, `createdAt`, `updatedAt`, `deletedAt`, `deletedBy`

### Order
- `id` (PK)
- `writer`, `supplierId`, `receiverId`, `productId`, `hubId`, `deliveryId`
- `status`, `productName`, `productQuantity`, `productPrice`, `totalPrice`
- `requestNotes`, `requestDates`
- `createdAt`, `deadLine`, `deletedAt`, `deletedBy`

---

# AccessGuard 권한 설정 (Order Service)

주문(Order) 서비스에서 사용자의 역할(Role)과 리소스 범위(ResourceScope)에 따른 액션(Action) 권한을 정의한 표입니다.

---

## 권한 설정 표

| 역할(Role)       | CREATE | READ | UPDATE | DELETE | 리소스 범위 제한 | 설명 |
|-----------------|--------|------|--------|--------|-----------------|------|
| MASTER           | ✅     | ✅   | ✅     | ✅     | 없음            | 모든 액션 허용 |
| HUB_MANAGER      | ✅     | ✅   | ✅     | ✅     | 담당 Hub        | 허브 관리자 권한은 본인 Hub에서만 가능 |
| DELIVERY_USER    | ❌     | ✅   | ❌     | ❌     | 담당 Hub        | 조회만 가능, 본인 Hub 범위 내 |
| COMPANY_STAFF    | 조건부  | ✅   | 조건부 | ❌     | 본인 Company    | - CREATE/UPDATE: 본인 업체(공급사/수신사) 주문만 가능<br>- DELETE: 불가 |

- ✅: 해당 액션 허용
- ❌: 해당 액션 불가 (시도 시 `BusinessException` 발생)
- 조건부: 특정 리소스 범위에 따라 허용 여부 결정

---

## 권한 검증 로직

- `AccessGuard.requiredPermission(Action action, AccessContext ctx, ResourceScope scope)` 사용
- MASTER: 모든 액션 허용
- HUB_MANAGER: 지정된 Hub에서만 모든 액션 허용
- DELIVERY_USER: 지정된 Hub에서 READ만 허용
- COMPANY_STAFF: 본인 Company 관련 주문 CREATE/UPDATE 가능, DELETE 불가

## 검증 시 예외 처리

- 인증 정보 없을 경우 → `BusinessException(ACCESS_DENIED)`
- 리소스 범위와 불일치 → `BusinessException(ACCESS_DENIED)`
- 필요한 리소스 정보 누락 → `BusinessException(INVALID_INPUT)`


## ✅ 유효성 검증

### Hub
- 이름, 주소, 지역 필수

### Order
- 상품 정보 필수
- 수량 ≥ 1, 정수만 허용
- 주문 상태별 처리 조건 적용
    - 취소 가능 상태: `PENDING`, `PAYMENT_COMPLETE`, `ORDER_CONFIRMED`
    - 환불 요청 가능 상태: `DELIVERY_COMPLETE`
    - 이미 취소 또는 환불 완료된 주문은 처리 불가


## 📌 Soft Delete 처리

- 엔티티 삭제 시 실제 데이터는 삭제되지 않고 `deletedAt`, `deletedBy`만 기록
- 조회 시 `deletedAt IS NULL` 조건으로 필터링

---

## 📌 QueryDSL 활용

- 동적 검색 조건 처리
- 페이징 및 정렬 지원
- Hub, Order 모듈에서 모두 적용