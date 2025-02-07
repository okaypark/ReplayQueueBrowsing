package solace.test.ReplayQueueBrowsing.solace;

import org.springframework.stereotype.Component;
import solace.test.ReplayQueueBrowsing.config.SEMPConfig;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Component
public class SEMPCommand {

    private String brokerUrl;
    private String username;
    private String password;
    private String messageVpn;

    public SEMPCommand(SEMPConfig config) {
        this.brokerUrl = config.getHost();  // Solace SEMP URL
        this.username = config.getUsername();   // SEMP 관리 계정
        this.password = config.getPassword();   // SEMP 관리 비밀번호
        this.messageVpn = config.getVpn();      // 대상 Message VPN
    }

    private String encodeBasicAuth() {
        String auth = username + ":" + password;
        System.out.println("SEMP username=" + username + " passowrd="+ password);
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public String replayQueue(String queueName, String replayStartTime) throws Exception {
        String resultMsg;
        // SEMPv2 Endpoint URL 구성
        String urlStr = String.format(
                "%s/SEMP/v2/action/msgVpns/%s/queues/%s/startReplay",
                brokerUrl, messageVpn, queueName
        );

        // HTTP 연결 생성
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Authorization", "Basic " + encodeBasicAuth());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);


        // JSON Body 생성 (시간 또는 전체 Replay)
        String body = "";
/*
        if (replayStartTime != null) {
            body = String.format(
                    "{\"replayStartLocation\": \"date\", \"replayStartTime\": \"%s\"}",
                    replayStartTime
            );
        } else {
            body = "{\"replayStartLocation\": \"beginning\"}";
        }

        // body 내용 출력 (디버깅용)
        System.out.println("Generated JSON Body: " + body);
*/
        // Request Body 전송
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // API 응답 처리
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
            resultMsg = "Replay command successfully sent for queue: " + queueName;
            System.out.println(resultMsg);
        } else {
            resultMsg = "Failed to send replay command. HTTP response code: " + responseCode;
            System.out.println(resultMsg);
        }

        conn.disconnect();
        return resultMsg;
    }


/*
    public static void main(String[] args) throws Exception {
        // Solace Broker 및 SEMP 관리 계정 정보
        String brokerUrl = "http://<broker-ip>:<management-port>"; // 예: http://localhost:8080
        String username = "admin";    // SEMP 관리 계정
        String password = "admin";    // SEMP 관리 비밀번호
        String messageVpn = "default"; // 대상 Message VPN
        String queueName = "myQueue";  // 대상 큐 이름

        // 재생 시작 시간 설정 (UTC ISO8601 형식: 예 "2023-01-01T00:00:00Z")
        String replayStartTime = null; // null인 경우 시작부터 모든 메시지

        // Replay Command 실행
        SolaceReplayCommand replayCommand = new SolaceReplayCommand(brokerUrl, username, password, messageVpn);
        replayCommand.replayQueue(queueName, replayStartTime);
    }
*/
}