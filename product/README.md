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
│ └─ com.ojo.mullyuojo.product
│ ├─ application
│ │ ├─ ProductService.java
│ │ ├─ dtos
│ │ │ ├─ ProductRequestDto.java
│ │ │ ├─ ProductResponseDto.java
│ │ │ └─ ProductSearchDto.java
│ │ └─ security
│ │ ├─ AccessContext.java
│ │ ├─ Role.java
│ │ └─ AccessGuard.java
│ ├─ controller
│ │ └─ ProductController.java
│ └─ domain
│ ├─ Product.java
│ ├─ ProductRepository.java
│ └─ ProductRepositoryImpl.java
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

🔒 접근 제어

AccessContext 기반으로 사용자 권한 및 소속 Hub/Company 검증

Action과 ResourceScope를 통해 CRUD 접근 제어 수행

⚙️ DTO / 엔티티 주의 사항
| 필드          | Nullable | 비고            |
| ----------- | -------- | ------------- |
| name        | false    | 공백/Null 체크 필요 |
| price       | false    | 10원 단위, 음수 불가 |
| stock       | false    | 1 이상, 음수 불가   |
| description | true     | 선택 입력         |
| companyId   | false    | 필수            |
| hubId       | false    | 필수            |
