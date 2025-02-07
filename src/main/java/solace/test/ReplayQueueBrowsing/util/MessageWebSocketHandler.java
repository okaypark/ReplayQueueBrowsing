package solace.test.ReplayQueueBrowsing.util;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArraySet<org.springframework.web.socket.WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection established: " + session.getId());
        sessions.add(session);
        System.out.println("Active sessions after addition: " + sessions.size());
    }

    @Override
    public void handleTextMessage(org.springframework.web.socket.WebSocketSession session, TextMessage message) throws Exception {
        // This can be used if client sends messages to server
    }

    @Override
    public void afterConnectionClosed(org.springframework.web.socket.WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void broadcast(String message) {
        System.out.println("WebSocket:broadcast method");
        System.out.println("WebSocket:broadcast method | Active sessions count: " + sessions.size());
        System.out.println("Sending message to session: " + sessions.toString() + " | Open: " + sessions.stream().anyMatch(org.springframework.web.socket.WebSocketSession::isOpen));

        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
                System.out.println("WebSocket:broadcast msg:" + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}