package solace.test.ReplayQueueBrowsing.config;

import javax.jms.Session;
import javax.jms.Connection;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class SessionConfig {

    // Solace 브로커 연결 정보
    @Value("${solace.host}")
    private String host;

    @Value("${solace.msgVpn}")
    private String vpn;

    @Value("${solace.clientUsername}")
    private String username;

    @Value("${solace.clientPassword}")
    private String password;

    private JCSMPProperties properties;

    /**
     * JMS API의 Session 객체를 생성하여 Bean으로 등록
     * @return Session 객체
     * @throws Exception
     */
    @Bean
    public Session solaceSession() throws Exception {
        // Solace connection factory 설정
        SolConnectionFactory factory = SolJmsUtility.createConnectionFactory();
        factory.setHost(host);
        factory.setVPN(vpn);
        factory.setUsername(username);
        factory.setPassword(password);

        // Connection 생성
        Connection connection = factory.createConnection();

        // Session 생성 (Not transacted, 자동 ACK)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 연결 시작
        connection.start();

        System.out.println("Solace JMS Session 생성 및 연결 성공!");

        return session;
    }



    // JSCMP 세션생성 (connect는 Service에서 관리)
    // @Bean에 등록을 해서 의존성 주입 : 다른 클래스 객체에서 생성자를 통해 의존성 주입하면 모든 클래스 공통사용가능(JSCMPSession)
    @Bean
    public JCSMPSession initJSCMPSession() throws JCSMPException {

        properties = new JCSMPProperties();

        // 호스트, 사용자 정보, 비밀번호 및 VPN 이름 설정
        properties.setProperty(JCSMPProperties.HOST, host); // Host:Port
        properties.setProperty(JCSMPProperties.USERNAME, username); // Username
        properties.setProperty(JCSMPProperties.PASSWORD, password); // Password
        properties.setProperty(JCSMPProperties.VPN_NAME, vpn); // VPN Name

        // 중복 구독시 무시하고 진행
        properties.setProperty(JCSMPProperties.IGNORE_DUPLICATE_SUBSCRIPTION_ERROR, true);

        // JCSMPSession 생성만 담당. 세션 반환해서 외부에서 연결 및 통신 처리(Service 파트)
        return JCSMPFactory.onlyInstance().createSession(properties);
    }

    public void setIgnoreDuplicateSubscriptionError() {
        properties.setProperty(JCSMPProperties.IGNORE_DUPLICATE_SUBSCRIPTION_ERROR, true);
    }
}