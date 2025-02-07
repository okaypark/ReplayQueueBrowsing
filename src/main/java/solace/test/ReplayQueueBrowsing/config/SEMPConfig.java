package solace.test.ReplayQueueBrowsing.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class SEMPConfig {

    // Solace SEMP 연결 정보
    @Value("${semp.host}")
    private String host;

    @Value("${semp.msgVpn}")
    private String vpn;

    @Value("${semp.clientUsername}")
    private String username;

    @Value("${semp.clientPassword}")
    private String password;

}