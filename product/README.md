# Product Service

## 📦 개요
`Product Service`는 상품 관리 기능을 제공하는 Spring Boot 애플리케이션입니다.  
상품 등록, 조회, 수정, 삭제, 재고 관리 기능을 포함하며, QueryDSL과 JPA를 기반으로 구현되었습니다.

---

## 🏗️ 기술 스택

| 구분 | 내용 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.x |
| 데이터베이스 | MySQL / MariaDB |
| ORM | Spring Data JPA |
| 쿼리 | QueryDSL |
| 인증/인가 | Custom AccessContext, AccessGuard |
| 빌드 | Gradle |
| 테스트 | JUnit 5, Mockito |

---

## 🗂️ 프로젝트 구조
src
└─ main
├─ java
│  └─ com
│     └─ ojo
│        └─ mullyuojo
│           └─ product
│              ├─ application
│              │  ├─ ProductService.java
│              │  ├─ dtos
│              │  │  ├─ ProductRequestDto.java
│              │  │  ├─ ProductResponseDto.java
│              │  │  └─ ProductSearchDto.java
│              │  └─ security
│              │     ├─ AccessContext.java
│              │     ├─ Role.java
│              │     └─ AccessGuard.java
│              ├─ controller
│              │  └─ ProductController.java
│              └─ domain
│                 ├─ Product.java
│                 ├─ ProductRepository.java
│                 └─ ProductRepositoryImpl.java
└─ resources
└─ application.yml


---

## 📝 주요 기능

### 1️⃣ 상품 조회
- **목록 조회**
```http
GET /products
   ```
Query Parameters:
| 파라미터        | 타입     | 설명                  |
| ----------- | ------ | ------------------- |
| name        | string | 상품명 검색 (부분일치)       |
| minPrice    | number | 최소 가격               |
| maxPrice    | number | 최대 가격               |
| minStock    | number | 최소 재고               |
| maxStock    | number | 최대 재고               |
| description | string | 설명 검색 (부분일치)        |
| page        | number | 페이지 번호 (0부터 시작)     |
| size        | number | 페이지 크기              |
| sort        | string | 정렬 (예: `price,asc`) |

정렬 가능 필드: name, price, stock, createdAt

```json
{
  "content": [
    {
      "id": 1,
      "name": "노트북",
      "price": 1200000,
      "stock": 50,
      "description": "고성능 노트북",
      "writer": "user1",
      "companyId": 1,
      "hubId": 1,
      "createdAt": "2025-08-21T09:00:00",
      "updatedAt": "2025-08-21T09:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 1,
  "size": 20,
  "number": 0,
  "empty": false
}

```

단건 조회
```http
GET /products/{id}
   ```

상품 생성
```http
POST /products
Content-Type: application/json
Headers:
- X-User-Id
- X-Role
- X-Company-Id
- X-Hub-Id
```
Request Body:

```http
{
  "name": "상품명",
  "price": 10000,
  "stock": 50,
  "description": "상품 설명",
  "companyId": 1,
  "hubId": 1
}
```
3️⃣ 상품 수정
```http
PATCH /products/{id}
Content-Type: application/json
Headers:
- X-User-Id
- X-Role
- X-Company-Id
- X-Hub-Id
```

Request Body 예시:
```jason
{
  "name": "수정된 상품명",
  "price": 12000,
  "stock": 40,
  "description": "수정된 설명"
}
```
4️⃣ 상품 삭제
```http
DELETE /products/{id}
Headers:
- X-User-Id
- X-Role
- X-Company-Id
- X-Hub-Id
```

5️⃣ 재고 차감
```http
POST /products/{id}/reduce-stock
Params:
- quantity: 차감할 수량
Headers:
- X-User-Id
- X-Role
- X-Company-Id
- X-Hub-Id
```
# AccessGuard 권한 설정 (Product Service)

상품(Product) 서비스에서 사용자의 역할(Role)과 리소스 범위(ResourceScope)에 따른 액션(Action) 권한을 정의한 표입니다.

---

## 권한 설정 표

| 역할(Role)       | CREATE | READ | UPDATE | DELETE | 리소스 범위 제한 | 설명 |
|-----------------|--------|------|--------|--------|-----------------|------|
| MASTER           | ✅     | ✅   | ✅     | ✅     | 없음            | 모든 액션 허용 |
| HUB_MANAGER      | ✅     | ✅   | ✅     | ✅     | 담당 Hub        | 허브 관리자 권한은 본인 Hub에서만 가능 |
| DELIVERY_USER    | ❌     | ✅   | ❌     | ❌     | 담당 Hub        | 조회만 가능, 본인 Hub 범위 내 |
| COMPANY_STAFF    | ❌     | ✅   | ✅     | ❌     | 본인 Company    | 삭제 불가, 본인 업체만 접근 가능 |

- ✅: 해당 액션 허용
- ❌: 해당 액션 불가 (시도 시 `BusinessException` 발생)

---

## 권한 검증 로직

- `AccessGuard.requiredPermission(Action action, AccessContext ctx, ResourceScope scope)` 사용
- MASTER: 모든 액션 허용
- HUB_MANAGER: 지정된 Hub에서만 모든 액션 허용
- DELIVERY_USER: 지정된 Hub에서 READ만 허용
- COMPANY_STAFF: 본인 Company에서 READ, UPDATE 가능, DELETE 불가

## 검증 시 예외 처리

- 인증 정보 없을 경우 → `BusinessException(ACCESS_DENIED)`
- 리소스 범위와 불일치 → `BusinessException(ACCESS_DENIED)`
- 필요한 리소스 정보 누락 → `BusinessException(INVALID_INPUT)`



🔒 접근 제어

AccessContext 기반으로 사용자 권한 및 소속 Hub/Company 검증
Action과 ResourceScope를 통해 CRUD 접근 제어 수행

