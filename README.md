<h1 align="center">Welcome to StepMate-Server 🏃‍♂️</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
  <a href="https://github.com/wupitch/wupitch-server/blob/main/LICENSE" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/github/license/wupitch/wupitch-server" />
  </a>
</p>  

> stepmate Back-End Server Project

<div align=center>
  <img width="200" alt="StepMate Logo" src="https://github.com/step-Mate/stepmate-server/assets/75068957/e5c64694-3adb-4b53-a33e-0a2c7e3306f8">
</div>

<div align=center>
  <img width="600" alt="StepMate Logo" src="https://github.com/apfhd12gk/Spring-Study/assets/75068957/67643a46-69d2-4b15-984e-5167692fd19a">
</div>


## API Doc
  ### [StepMate Server Swagger](http://stepmate.shop/swagger-ui/index.html)    

## ACCESS APP STORE
  ### [Google Play Store](https://play.google.com/store/apps/details?id=com.stepmate.app)

## 기술스택

<p>
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/>
  <img src="https://img.shields.io/badge/JPA-red?style=flat-square"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-black?style=flat-square"/>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/>
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=Thymeleaf&logoColor=white"/>
</p>
<p>
  <img src="https://img.shields.io/badge/AWS-FF9900?style=flat-square&logo=Amazon EC2&logoColor=white"/>
  <img src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=NGINX&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>
</p>

## 개발환경
- BackEnd
  - java 17
  - spring boot 3.1.7
  - gradle

## 핵심기능
### 회원가입/로그인 
- 회원가입 아이디, 비밀번호, 닉네임 생성 시 규칙에 맞게 생성 할 수 있도록 입력값 유효성 검증
- 이메일 인증 서비스를 통해 아이디 또는 비밀번호 분실시 이메일 인증을 통해 찾기 가능
  - 이메일 인증은 만료시간이 있는 인증번호를 `Redis`를 통해 관리
- 회원가입과 로그인을 통해 jwt 발급 받아 특정 리소스에 접근할 권한이 있는지 확인하는데 사용
  - jwt 탈취에 대한 보안 문제는 리프레쉬 토큰을 `Redis`에 관리함으로 탈취 문제 개선
- 회원가입시 비밀번호와 칼로리 계산을 위한 신체정보를 암호화하여 데이터베이스에 저장하여 기밀성 유지

### 미션
- 신체정보와 걸음 수를 칼로리 계산법에 대입하여 칼로리 소모량을 계산하여 칼로리 미션에 값 계산 에 사용
- 스케줄러와 크론 표현식을 통해 주간 미션을 매주 월요일 자정에 미션 초기화
- 칼로리와 걸음 수와 관련된 미션 목푯값에 도달 시 미션 보상인 경험치와 칭호를 부여

### 랭킹
- 랭킹 보드에 걸음 수 순위를 닉네임, 레벨, 걸음 수, 칭호, 순위, 전일 대비 순위 상승률 표시
  - 친구 랭킹 보드에 친구가 맺어진 사용자들 대상으로 친구 순위를 계산하여 순위와 정보들을 표시
- 유저 일일 걸음 수를 내림차순으로 순위를 부여 걸음 수 값이 같을 경우 레벨, 이름 순으로 정렬 
- 랭킹 보드에 표시된 사용자 클릭시 클릭된 사용자 상세 정보를 표시 
  - 사용자 상세 정보는 닉네임, 레벨, 걸음 수, 칭호, 순위, 전일 대비 순위 상승률 표시 진행중인 미션 타입별로 표시, 이번달 월간 걸음 수를 표시
- 스케줄러를 사용하여 랭킹 보드의 걸음 수 순위를 매일 자정에 순위를 업데이트
## 시스템 구성도

![시스템 구성도 drawio (1)](https://github.com/step-Mate/stepmate-server/assets/75068957/523d7faa-4400-477b-ad33-676987c6760a)


## ERD

![ERD](https://github.com/step-Mate/stepmate-server/assets/75068957/19ca5965-950d-462d-a9e0-efbbb5b7ccf7)
