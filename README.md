<h1>📦 프로젝트 명: 재입고 알림 시스템</h1><br>
<h2>📋 프로젝트 개요</h2><br>
Spring Boot 기반의 재입고 알림 시스템.<br>
사용자가 특정 상품에 대한 재입고 알림을 신청하면<br>
상품 재입고 시 알림을 자동으로 전송해주는 기능을 제공.<br>
REST API를 기반으로 구현 Redis, MySQL 등의 기술 스택을 활용.<br><br>

<h2>⚙️ 주요 기능</h2>
상품 등록 : 새로운 상품을 추가하고 관리합니다.<br>
재입고 알림 신청 : 사용자가 특정 상품의 재입고 알림을 신청할 수 있습니다.<br>
재입고 알림 전송 : 상품 재입고 시 신청한 사용자에게 알림을 전송합니다.<br>
알림 기록 관리 : 알림 전송 이력을 저장하고 확인할 수 있습니다.<br>

<h2>🛠️ 기술 스택</h2>
Backend: Spring Boot, Spring MVC, Spring Data JPA<br>
Database: MySQL<br>
Cache: Redis<br>
Build Tool: Gradle<br>
Version Control: Git<br>
Containerization: Docker & Docker Compose<br><br>

<h2>📂 프로젝트 구조</h2>
src/<br>
├── main/<br>
│   ├── java/<br>
│   │   └── com.sparta.task2/<br>
│   │       ├── controller/            # 컨트롤러 레이어<br>
│   │       ├── dto/                   # 요청 및 응답 DTO 클래스<br>
│   │       ├── entity/                # 데이터베이스 엔티티 클래스<br>
│   │       ├── repository/            # JPA 레포지토리 인터페이스<br>
│   │       ├── service/               # 서비스 로직 클래스<br>
│   │       └── configuration/         # Redis 설정<br>
│   └── resources/<br>
│       ├── application.yml            # 애플리케이션 설정 파일<br>
│       └── data.sql                   # 초기 데이터베이스 설정<br>
└── test/                              # 테스트 코드 작성 실패로 비활성화<br><br>

<h2>🚀 실행 방법</h2>

MySQL 및 Redis 설치 및 실행 || Docker Compose<br><br>

<h2>🧪 API 명세</h2>
1. 상품 등록<br><br>
URL: POST /product/add<br>
Request Body:<br><br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"productId": {상품아이디},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"restockRound": 0<br>
}<br><br>
Response : "Product added successfully"<br>

2. 특정 상품의 재입고 알림 신청한 유저 등록<br><br>
URL: /products/{상품아이디}/notification/registerUser<br>
Request Body: None<br><br>
Response : "User notification registered successfully."<br><br>

4. 재입고 알림 전송<br>
URL: POST /products/{상품아이디}/notifications/re-stock<br>
Request Body:<br><br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"productId" : {상품아이디},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"quantity" : {재입고수량}<br>
}<br><br>
Response : "Normal User notification send successfully."

5. 관리자용 재입고 알림 전송<br>
URL: POST /products/admin/{상품아이디}/notifications/re-stock<br>
Request Body:<br><br>
{<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"productId" : {상품아이디},<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"quantity" : {재입고수량}<br>
}<br><br>
Response : "Admin notification send successfully."


