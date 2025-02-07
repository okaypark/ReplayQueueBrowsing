package solace.test.ReplayQueueBrowsing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import solace.test.ReplayQueueBrowsing.util.MessageWebSocketHandler;



/**
 * WebSocketConfig:
 * WebSocket 통신을 설정 : Events Broker에서 수신한 메세지를 웹페이지로 푸핑처리
 * Spring WebSocket이 활성화되도록 설정하며, 메시지를 처리할 핸들러를 매핑합니다.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageWebSocketHandler messageWebSocketHandler;

    // WebSocket 메세지 핸들러
    public WebSocketConfig(MessageWebSocketHandler messageWebSocketHandler) {
        this.messageWebSocketHandler = messageWebSocketHandler;
    }

    // 핸들러 별도관리 Class (WebSocketHandlerRegistry) 핸들러로 등록.
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageWebSocketHandler, "/ws/messages").setAllowedOrigins("*");
    }
}