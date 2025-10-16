# 🌱 DailyGrowth – 프로젝트 & 할 일 관리 서비스 
> ⚠️ readme 수정중으로 일부 이동되지 않는 링크가 있습니다.

DailyGrowth는 개인의 목표 달성과 팀 협업 효율을 높이기 위한 프로젝트·할 일(Task) 통합 관리 서비스입니다.
그리고 이 과정에서 발생하는 문제들을 찾아 해결하여 안정적인 예매 시스템을 구축하는 것을 목표로 합니다.
<br>
<br>
## 목차
| 번호 | 섹션       | 설명                  |
| -- | -------- | ------------------- |
| 1  | [프로젝트 개요](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)  | 프로젝트 소개 및 개발 목적 |
| 2  | [주요 기능](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5)    | 핵심 기능 및 특징 |
| 3  | [트러블 슈팅](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)   | 개발 중 발생한 문제점과 해결 과정 |
| 4  | [시스템 아키텍처](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B7%B8%EB%A6%BC%EC%9C%BC%EB%A1%9C-%EA%B0%84%ED%8E%B8%ED%99%94-%ED%95%A0%EA%B2%83) | 전체 시스템 구조 및 기술 스택 |
| 5  | [프로젝트 플로우](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%94%8C%EB%A1%9C%EC%9A%B0) | 사용자 흐름 및 비즈니스 로직 |
| 6  | [ERD](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#erd)      | 엔티티 관계 다이어그램 |
| 7  | [프로젝트 구조](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0)      | 코드 구조 |
| 8  | [실행 방법](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%8B%A4%ED%96%89-%EB%B0%A9%EB%B2%95)    | 로컬 환경 실행 가이드 |
| 9  | [실제 서비스](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%8B%A4%EC%A0%9C-%EC%84%9C%EB%B9%84%EC%8A%A4)   | 배포 및 운영 정보 |
<br>

> 각각의 자세한 내용은 Wiki로 이동하는 링크에서 확인 할 수 있습니다.
<br>
<br>
## 프로젝트 개요
### 개발 목적
이 프로젝트는 “매일의 작은 성장이 모여 큰 변화를 만든다”는 철학을 기반으로,
개인의 작업 루틴과 프로젝트를 시각화하고 추적할 수 있는 생산성 관리 도구입니다.

### 개발 목표
	•	✅ 헥사고날 아키텍처를 도입하여 외부 의존성 낮추기
	•	✅ Spring Security (OAuth2.0, JWT)를 통한 인증 시스템 구축
	•	✅ Docker, CloudFlare, EC2를 활용한 개발 및 운영환경 구축

프로젝트 기간: 2025.04 ~ 개발·운영 중
참여 인원: 개인프로젝트
<br>
<br>
## 주요 기능
- **프로젝트 관리**: 프로젝트 생성하고 할 일의 완료 정도에 따라 진행률을 추적 관리 할 수 있습니다.
- **할 일(Task) 관리**: 하단 + 버튼으로 손쉽게 할 일 추가할 수 있습니다.
- **사용자 인증**: OAuth 2.0 및 JWT를 통해 Kakao 계정으로 간편하고 안전하게 로그인할 수 있습니다.
<br>
<br>

## 트러블 슈팅
| Category         | Topic                                                                         | Detailed Wiki Link |
| ---------------- | ----------------------------------------------------------------------------- | ------------------ |
| **Security**     | Refresh Token 재발급 보안 강화 아키텍처 설계 | 메인 문서로 이동 |
|                  | └ JWT 단일 구조 취약점 진단 및 개선 | 자세히 보기 |
|                  | └ Redis 기반 상태관리 (Stateful Refresh Token) 적용 | 자세히 보기 |
|                  | └ IP / User-Agent 검증 절차 강화 및 쿠키 보안 옵션 적용 | 자세히 보기 |
| **Architecture** | 인증/인가 아키텍처 (Access / Refresh 흐름) 설계도 | 메인 문서로 이동 |
|                  | └ Stateless vs Stateful 토큰 설계 비교 | 자세히 보기 |
|                  | └ TokenService 책임 분리 (OAuthService → AuthService → TokenService → CookieUtil) | 자세히 보기 |
| **Infra**        | 데이터베이스 부하 및 레플리케이션 문제 | 메인 문서로 이동 |
|                  | └ Docker / EC2 배포 관련 리소스(디스크, 메모리) 이슈 | 자세히 보기 |
| **Concurrency**  | 동시성 문제 (좌석 선점/자원 경합 → 예: 할일 동시 수정 충돌) | 메인 문서로 이동 |
|                  | └ 낙관적 락(ObjectOptimisticLockingFailureException) 처리 | 자세히 보기 |
|                  | └ 트랜잭션 경계와 재시도 전략(비동기 작업 포함) | 자세히 보기 |
<br>
<br>

## 시스템 아키텍처 (그림으로 간편화 할것)

| 카테고리 | 기술 | 설명 |
|-----------|------------------------------|----------------------------|
| Backend | Java 17 + Spring Boot 3.5.4 | 메인 애플리케이션 |
| Database | MySQL | RDB |
| Cache | Redis | RefreshToken 상태 관리 및 캐싱 |
| Security | Spring Security + OAuth 2.0 + JWT | 인증/인가 |
| CI/CD | GitHub Actions | 테스트 및 배포 자동화 파이프라인 |
| Infrastructure | Docker + Docker Compose, AWS EC2 | 컨테이너 기반 배포 |
| Docs | Springdoc (Swagger UI) | API 문서 자동화 |
| Frontend | React + Vite + TailwindCSS | SPA 기반 UI |
<br>
<br>

## 프로젝트 플로우
| 구분 | 주요 역할 |
| --- | ------- |
| 2-Mode 로그인 플로우 | Wiki로 이동|
<br>
<br>

## ERD

<img width="2410" height="968" alt="image" src="https://github.com/user-attachments/assets/f635b56f-833c-45f8-824c-e58ee8a73e04" />

<br>
<br>

## 프로젝트 구조
<pre>
├── config
│   ├── security
│   │   ├── jwt
│   │   ├── principal
│   │   ├── service
│   │   └── token
├── domain
│   ├── auth
│   │   ├── domain
│   │   ├── infrastructure
│   │   └── presentation
│   ├── color
│   │   ├── application
│   │   ├── entity
│   │   ├── infrastructure
│   │   ├── port
│   │   └── presentation
│   ├── member
│   │   ├── application
│   │   ├── domain
│   │   ├── infrastructure
│   │   └── presentation
│   ├── project
│   │   ├── application
│   │   ├── domain
│   │   ├── infrastructure
│   │   └── presentation
│   └── task
│       ├── application
│       ├── domain
│       ├── infrastructure
│       └── presentation
└── GreencatsoftwareApplication.java
</pre>
<br>
<br>

## 실행 방법
#### 1. 프로젝트 클론
```bash
git clone https://github.com/jamm0316/dailyGrowth.git
```

#### 2. 환경 변수 설정
프로젝트 루트 디렉터리에 .env 파일 생성 후 아래에 필요한 변수들 채우기
```bash
# MySQL 설정
MYSQL_ROOT_PASSWORD={yourPassword}
MYSQL_DATABASE={yourDbName}
MYSQL_USER={yourDbUserName}
MYSQL_PASSWORD={yourPassword}

# Redis 설정
REDIS_PASSWORD={yourPassword}

# OAuth2 설정
OAUTH2_PROVIDERS_KAKAO_REST_API_KEY={KakaoOauthRestAPIKey}
OAUTH2_PROVIDERS_KAKAO_REDIRECT_URI=https://dailygrowth.shop/api/v1/oauth/callback/kakao

# JWT 설정
JWT_ISSUER={yourIssuer}
JWT_SECRET_KEY={yourSecretKey}
JWT_ACCESS_TOKEN_EXPIRATION=1800000

# JPA 설정
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
```
#### 3. 빌드 및 실행
Docker Compose를 사용하여 프로젝트의 모든 서비스를 한 번에 빌드하고 실행
```bash
./gradlew build
docker-compose -f docker-cmopose.yml up -build
```

#### 4. 애플리케이션 접속
웹 브라우저에서 http://localhost:8080 으로 접속하여 애플리이션을 확인 가능
<br>
<br>

## 실제 서비스
- 배포 URL: https://dailygrowth.shop
- GitHub Repository: https://github.com/jamm0316/dailygrowth
- CI/CD 구성: GitHub Actions + Docker Compose + EC2
