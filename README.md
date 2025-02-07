# Solace REPLAY , Queue BORWSING (JCSMP) 예제

**Solace 메시징 시스템에서 Queue Replay 실행(SEMP) 및 브라우징 작업을 수행할 수 있는 웹 애플리케이션입니다. 이 예제는 Spring Boot 백엔드와 함께 웹소켓을 사용하며, JavaScript와 HTML 기반 프런트엔드로 구성되어 있습니다.

---

## 🔑 접속 정보 및 계정 정보 설정

### 1. application.yml 설정 (예시)
Solace 메시징 시스템의 접속 정보와 사용자 계정은 `src/main/resources/application.yml`에서 설정할 수 있습니다.

```yaml
solace:
  host: tcp://<SOLACE_HOST>:55555
  vpn-name: <YOUR_VPN_NAME>
  username: <YOUR_USERNAME>
  password: <YOUR_PASSWORD>

server:
  port: 8081

semp:
  host: "http://localhost:8080"
  msgVpn: "TestVPN"            # 메시지 VPN 이름
  clientUsername: "admin"      # 클라이언트 사용자 이름
  clientPassword: "admin"      # 클라이언트 비밀번호

```
semp 접속 정보는 admin만 실행이 됩니다., 일반 유저는 권한 설정하면 가능 client Profile, ACL Profile

---

## 🖥️ 실행 방법

### 1. 프로젝트 클론
```bash
git clone https://github.com/[YOUR_USERNAME]/solace-queue-operations.git
cd solace-queue-operations
```

### 2. 서버 실행

프로젝트는 Maven으로 빌드되며, 다음 명령어로 Spring Boot 애플리케이션을 실행할 수 있습니다:

#### Maven Command
```bash
./mvnw spring-boot:run
```

#### 또는 직접 JAR 파일 실행
1. JAR 파일 빌드:
    ```bash
    ./mvnw clean package
    ```
2. 빌드된 JAR 파일 실행:
    ```bash
    java -jar target/solace-queue-operations-0.0.1-SNAPSHOT.jar
    ```

이예제는 기본적으로 `http://localhost:8081`에서 실행됩니다.

---

## 📋 주요 기능

- **Replay Command**: 특정 Queue에서 메시지를 다시 재생합니다. SEMP 
- **Browse Queue**: Queue에 수신된 메시지를 브라우징합니다. JCSMP
- **WebSocket 지원**: 실시간 메시지 수신 데이터 표시.

---

## 🚀 기술 스택

### 프런트엔드
- **HTML, CSS**: 사용자 인터페이스를 구성.
- **JavaScript**: 웹 페이지 로직 및 WebSocket 통신 연결.

### 백엔드
- **Spring Boot**: API 서버 및 비즈니스 로직 처리.
- **Java 17**: 최신 Java SDK로 개발.
- **Jakarta EE**: `Jakarta` 라이브러리를 이용한 기능 구성.

### 메시징 시스템
- **Solace**: 메시징 Queue, REPLAY

---

## 📖 API 설명서

### 1. **Replay Queue**
- **URL**: `/api/replay/replayCommand`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "queueName": "QUEUE_NAME"
  }
  ```
- **Response**: Replay 작업 결과에 대한 상태 메시지.

### 2. **Browse Queue**
- **URL**: `/api/replay/browsingQueue`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "queueName": "QUEUE_NAME"
  }
  ```
- **Response**: 큐를 브라우징하여 메시지 반환.

---

## 🖥️ WebSocket 연결

- **WebSocket URL**: `ws://localhost:8081/ws/messages`
- 클라이언트는 WebSocket을 통해 실시간 데이터를 수신하며, 수신된 메시지는 사용자 인터페이스에 표시됩니다.

---

## 📂 프로젝트 구조

```markdown
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.solaceQueueOperations  // Java 백엔드 코드
│   │   ├── resources
│   │   │   ├── static  // 정적 파일 (JavaScript, HTML, CSS)
│   │   │   ├── templates // (추가적인 템플릿 파일)
│   └── test
│       └── java // 테스트 코드
├── README.md  // 프로젝트 설명
└── pom.xml    // 빌드 파일
```

---

## 📌 참고 사항

1. `Solace` 메시징 브로커가 올바르게 설정되고 실행 중인지 확인하세요.
2. WebSocket 연결은 로컬 환경에서 `ws://localhost:8081/ws/messages` 경로를 사용하여 설정됩니다.
3. URL이나 포트 번호는 운영 환경에 맞게 수정하세요.

