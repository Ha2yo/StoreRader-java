# StoreRader-Java

지역 기반 매장 정보와 상품 가격 정보를 조회하고 비교할 수 있는 웹 서비스입니다.  
지도 UI를 통해 매장을 탐색하고, 가격 데이터를 조회하며, 관리자 화면에서 공공데이터 수집과 기초 데이터 관리를 수행할 수 있습니다.

운영 주소:

- 서비스: [https://store-rader.shop](https://store-rader.shop)
- API 문서: [https://store-rader.shop/api](https://store-rader.shop/api)

## 주요 기능

- 지도 기반 매장 탐색
- 상품 가격 조회
- 지역 코드 조회
- 사용자 선호도 조회
- 사용자 매장 선택 로그 저장
- Google 로그인 기반 인증
- 관리자용 데이터 조회 및 공공데이터 수집

## 기술 스택

- Backend: Java 21, Spring Boot 3.4, Spring Web, Spring Data JPA, Flyway, PostgreSQL, JWT
- Frontend: React 19, TypeScript, Vite, React Router, React Bootstrap, Leaflet
- Infra: Docker Compose, Nginx
- API Docs: Springdoc OpenAPI / Swagger UI

## 프로젝트 구조

```text
StoreRader-Java/
├─ backend/        Spring Boot API 서버
├─ frontend/       React + Vite 프론트엔드
├─ deploy/         배포 관련 파일
├─ scripts/        보조 스크립트
├─ docker-compose.yml
└─ deploy.sh
```

## 백엔드 도메인 구성

`backend/src/main/java/com/storerader/server/domain` 기준:

- `auth`: Google 로그인, 토큰 재발급, 내 정보 조회, 로그아웃
- `admin`: 관리자 데이터 조회, 공공데이터 수집
- `good`: 상품 조회
- `price`: 가격 조회
- `regioncode`: 지역 코드 조회
- `store`: 매장 조회
- `userPreference`: 사용자 선호도 조회
- `userSelectionLog`: 사용자 선택 로그 저장

## 실행 환경

- Java: `21`
- Node.js: `18+` 또는 `20+` 권장
- PostgreSQL: `16`

### 3. API 문서 확인

로컬 실행 후 Swagger UI 경로:

- `/`
- 또는 리버스 프록시 환경에서는 `/api`

실제 경로는 배포 환경의 프록시 설정에 따라 달라질 수 있습니다.

## Docker Compose 실행

루트의 `docker-compose.yml`은 다음 구성을 기준으로 작성되어 있습니다.

- `database`: PostgreSQL 16
- `backend-blue`: Spring Boot 백엔드 블루 인스턴스
- `backend-green`: Spring Boot 백엔드 그린 인스턴스
- `frontend`: Nginx 기반 프론트엔드

## 환경 변수

실행 전 아래 값들을 환경에 맞게 준비해야 합니다.

백엔드 / Docker:

- `DB_USER`
- `DB_PASSWORD`
- `DB_NAME`
- `GOOGLE_CLIENT_ID`
- `JWT_SECRET`
- `APP_COOKIE_SAME_SITE`
- `APP_COOKIE_SECURE`
- `PUBLIC_API_KEY`
- `VWORLD_API_KEY`

프론트엔드 예시:

- `VITE_GOOGLE_CLIENT_ID`
- `VITE_GOOGLE_REDIRECT_URI`
- `VITE_API_SERVER_URL`

## 데이터베이스

- PostgreSQL 사용
- Flyway 마이그레이션 사용
- 마이그레이션 파일 위치:
  `backend/src/main/resources/db/migration`

테이블 생성과 변경은 애플리케이션 시작 시 Flyway를 통해 반영됩니다.

## API 개요

현재 확인되는 주요 엔드포인트 그룹:

- `/auth`
- `/admin`
- `/goods`
- `/prices`
- `/region-codes`
- `/stores`
- `/user-preference`
- `/user-selection`

관리자 영역에서는 공공데이터 수집용 SSE 스트림 엔드포인트도 사용합니다.

## 문서 위치

- 루트 README: 프로젝트 전체 개요
- 백엔드 설정: `backend/src/main/resources/application.properties`
- DB 마이그레이션: `backend/src/main/resources/db/migration`
- Docker 구성: `docker-compose.yml`