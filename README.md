# 🌱 DailyGrowth – 프로젝트 & 할 일 관리 서비스 
> ⚠️ readme 수정중으로 일부 이동되지 않는 링크가 있습니다.

DailyGrowth는 개인의 목표 달성과 팀 협업 효율을 높이기 위한
프로젝트·할 일(Task) 통합 관리 서비스입니다.

## 📑 목차
| 번호 | 섹션       | 설명                  |
| -- | -------- | ------------------- |
| 1  | [프로젝트 개요](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)  | 서비스 소개 및 개발 목적      |
| 2  | [주요 기능](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5)    | 핵심 기능 및 특징          |
| 3  | [트러블 슈팅](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)   | 개발 중 발생한 문제점과 해결 과정 |
| 4  | [시스템 아키텍처](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EA%B7%B8%EB%A6%BC%EC%9C%BC%EB%A1%9C-%EA%B0%84%ED%8E%B8%ED%99%94-%ED%95%A0%EA%B2%83) | 전체 시스템 구조 및 기술 스택   |
| 5  | [프로젝트 플로우](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%94%8C%EB%A1%9C%EC%9A%B0) | 사용자 흐름 및 주요 시나리오    |
| 6  | [ERD](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#erd)      | 엔티티 관계 다이어그램        |
| 7  | [프로젝트 구조](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0)      | 단위 테스트 및 검증 구조      |
| 8  | [실행 방법](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%8B%A4%ED%96%89-%EB%B0%A9%EB%B2%95)    | 로컬 환경 실행 가이드        |
| 9  | [실제 서비스](https://github.com/jamm0316/dailyGrowth?tab=readme-ov-file#%EC%8B%A4%EC%A0%9C-%EC%84%9C%EB%B9%84%EC%8A%A4)   | 배포 및 운영 정보          |

## 프로젝트 개요
### 개발 목적
DailyGrowth는 “매일의 작은 성장이 모여 큰 변화를 만든다”는 철학을 기반으로,
개인의 작업 루틴과 프로젝트를 시각화하고 추적할 수 있는 생산성 관리 도구입니다.

### 개발 목표
	•	✅ 프로젝트 및 할 일(Task) CRUD 관리
	•	✅ 프로젝트 진행률 자동 계산 및 시각화
	•	✅ Redis 기반 Refresh Token 상태관리로 보안 강화
	•	✅ React 기반 SPA + Spring Boot API 서버 통합 구조

프로젝트 기간: 2025.04 ~ 개발·운영 중
참여 인원: 개인프로젝트

## 주요 기능
- 프로젝트 관리: 프로젝트 생성하고 할 일의 완료 정도에 따라 진행률을 추적 관리 할 수 있습니다.
- 할 일(Task) 관리: 하단 + 버튼으로 손쉽게 할 일 추가할 수 있습니다.
- 사용자 인증: OAuth 2.0 및 JWT를 통해 Kakao 계정으로 간편하고 안전하게 로그인할 수 있습니다.

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


## 시스템 아키텍처 (그림으로 간편화 할것)

| 카테고리 | 기술 | 설명 |
|-----------|------------------------------|----------------------------|
| Backend | Java 17 + Spring Boot 3.x | 메인 애플리케이션 |
| Database | MySQL | RDB |
| Cache | Redis | RefreshToken 상태 관리 및 캐싱 |
| Security | Spring Security + OAuth 2.0 + JWT | 인증/인가 |
| Infra | Docker, GitHub Actions, AWS EC2 | 컨테이너 기반 배포 및 CI/CD |
| Docs | Springdoc (Swagger UI) | API 문서 자동화 |
| Frontend | React + Vite + TailwindCSS | SPA 기반 UI |

## 프로젝트 플로우
| 구분 | 주요 역할 |
| --- | ------- |
| ~플로우 | Wiki로 이동|

## ERD

DB 테이블: color, project, task
ERD 다이어그램 이미지: (채워야 함 – ERD 이미지 또는 링크)

## 프로젝트 구조

## 실행 방법

### 백엔드(Spring Boot)
	1.	application.properties 작성 (DB 연결 정보 설정)
	2.	schema.sql / initial_data.sql 로 DB 구조 및 기초데이터 로드
	3.	빌드 및 실행

```bash
./gradlew build
./gradlew bootRun
```
Swagger: http://localhost:8080/swagger-ui.html

⸻

### 프론트엔드(React/Vite)
```bash
cd frontend
npm install
npm run dev
```

기본 접속: http://localhost:5173
필요 시 .env에 VITE_API_BASE_URL=http://localhost:8080 설정

⸻

### 테스트

Framework  JUnit5
위치  src/test/java/com/todoservice/greencatsoftware/domain/
테스트 항목  Color, Project, Task 엔티티 및 서비스 검증

./gradlew test

(채워야 함: 테스트 커버리지 % 또는 대표 테스트 사례 요약)

## 실제 서비스
- 배포 URL: https://dailygrowth.shop
- GitHub Repository: https://github.com/jamm0316/dailygrowth
- CI/CD 구성: GitHub Actions + Docker Compose + EC2
