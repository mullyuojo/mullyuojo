# Mullyuojo Company Module

Mullyuojo 프로젝트의 `Company` 모듈로, 업체 정보 관리와 관련된 REST API와 서비스 레이어를 제공합니다.  
주요 기능은 **업체 목록 조회, 단일 조회, 생성, 수정, 삭제**입니다.

---

## 목차

- [설명](#설명)
- [기술 스택](#기술-스택)
- [API 엔드포인트](#api-엔드포인트)
- [도메인 모델](#도메인-모델)
- [서비스 구조](#서비스-구조)
- [검색 및 페이징](#검색-및-페이징)
- [권한 검증](#권한-검증)

---

## 설명

`CompanyController`를 통해 REST API를 제공하며, `CompanyService`에서 실제 비즈니스 로직을 수행합니다.  
`CompanyRepository`와 QueryDSL을 활용하여 조건별 검색 및 페이징 처리를 지원합니다.  
Soft delete 전략을 적용하여 삭제된 업체는 DB에서 조회되지 않습니다.

---

## 기술 스택

- Java 17+
- Spring Boot
- Spring Data JPA
- QueryDSL
- Lombok
- Jakarta Validation
- Auditing (createdBy, createdDate, lastModifiedDate)
- PostgreSQL / MySQL 등 관계형 DB

---

## API 엔드포인트

| Method | URI                   | 설명                       | Request Body                   |
|--------|----------------------|----------------------------|--------------------------------|
| GET    | /companies           | 업체 목록 조회             | Query Parameters (CompanySearchDto) |
| GET    | /companies/{id}      | 단일 업체 조회            | -                              |
| POST   | /companies           | 업체 생성                 | CompanyRequestDto              |
| PATCH  | /companies/{id}      | 업체 수정                 | CompanyRequestDto              |
| DELETE | /companies/{id}      | 업체 삭제 (Soft delete)   | -                              |

**공통 헤더**

```http
X-User-Id: <사용자 ID>
X-Role: <권한(Role)>
X-Company-Id: <회사 ID>
X-Hub-Id: <허브 ID>
```
## 서비스 구조

### CompanyService

- `getCompanies`: 검색 조건과 페이징 기반 업체 목록 조회
- `getCompanyById`: 단일 업체 조회
- `createCompany`: 업체 생성 (권한 검증 포함)
- `updateCompany`: 업체 수정 (권한 검증 포함)
- `deleteCompany`: Soft delete 처리 (권한 검증 포함)

---

### 검색 및 페이징

- `CompanySearchDto` 기반 검색 가능:
    - `id`
    - `companyId`
    - `hubId`
    - `productId`
    - `name` (부분검색, 대소문자 무시)
    - `address` (부분검색, 대소문자 무시)

- `Pageable`을 통해 페이지 번호, 사이즈, 정렬 가능
- QueryDSL `OrderSpecifier`를 활용하여 동적 정렬 지원

---

### CompanyRepository / CompanyRepositoryImpl

- QueryDSL 기반 검색
- 조건별 `BooleanBuilder` 사용
- 페이징 및 정렬 지원

---

### 권한 검증

- `AccessContext`: 사용자 ID, Role, CompanyId, HubId 정보를 포함
- `AccessGuard`: Action 및 ResourceScope 기반 권한 검증
    - CREATE, UPDATE, DELETE 권한 체크
    - Scope: 회사 및 허브 단위

#### 업체 생성 요청
```http
POST /companies
X-User-Id: user123
X-Role: ADMIN
X-Company-Id: 1
X-Hub-Id: 1
Content-Type: application/json

{
    "companyId": 1,
    "hubId": 1,
    "productId": 2,
    "type": "MANUFACTURER",
    "name": "Example Company",
    "address": "Seoul, Korea"
}

```

#### 업체 수정 요청
```http
PATCH /companies/1
X-User-Id: user123
X-Role: ADMIN
X-Company-Id: 1
X-Hub-Id: 1
Content-Type: application/json

{
    "name": "Updated Company Name",
    "address": "Updated Address"
}

```

#### 업체 삭제 요청
```http
DELETE /companies/1
X-User-Id: user123
X-Role: ADMIN
X-Company-Id: 1
X-Hub-Id: 1

```

# AccessGuard 권한 설정 (Company Service)

회사 서비스에서 사용자의 역할(Role)과 리소스 범위(ResourceScope)에 따른 액션(Action) 권한을 정의한 표입니다.

---

## 권한 설정 표

| 역할(Role)       | CREATE | READ | UPDATE | DELETE | 리소스 범위 제한 | 설명 |
|-----------------|--------|------|--------|--------|-----------------|------|
| MASTER           | ✅     | ✅   | ✅     | ✅     | 없음            | 모든 액션 허용 |
| HUB_MANAGER      | ✅     | ✅   | ✅     | ✅     | 담당 Hub        | 허브 관리자 권한은 본인 Hub에서만 가능 |
| DELIVERY_USER    | ❌     | ✅   | ❌     | ❌     | 담당 Hub        | 조회만 가능, 본인 Hub 범위 내 |
| COMPANY_STAFF    | ❌     | ✅   | ✅     | ❌     | 본인 Company    | 생성 및 삭제 불가, 본인 업체만 접근 가능 |

- ✅: 해당 액션 허용
- ❌: 해당 액션 불가 (시도 시 `BusinessException` 발생)

---

## 권한 검증 로직

- `AccessGuard.requiredPermission(Action action, AccessContext ctx, ResourceScope scope)` 사용
- MASTER: 모든 액션 허용
- HUB_MANAGER: 지정된 Hub에서만 모든 액션 허용
- DELIVERY_USER: 지정된 Hub에서 READ만 허용
- COMPANY_STAFF: 본인 Company에서 READ, UPDATE 가능, CREATE/DELETE 불가

## 검증 시 예외 처리

- 인증 정보 없을 경우 → `BusinessException(ACCESS_DENIED)`
- 리소스 범위와 불일치 → `BusinessException(ACCESS_DENIED)`
- 필요한 리소스 정보 누락 → `BusinessException(INVALID_INPUT)`


## 참고 사항

- **Soft delete 전략** 사용
    - 실제 데이터는 삭제되지 않으며, `deletedAt` 및 `deletedBy`를 기록

- **낙관적 락 (@Version)** 사용
    - 동시성 충돌 방지

- **Validation**
    - 이름, 주소, ID 값에 대한 유효성 체크 포함
