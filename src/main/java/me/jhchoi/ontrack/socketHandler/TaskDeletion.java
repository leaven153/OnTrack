package me.jhchoi.ontrack.socketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDeletion extends TextWebSocketHandler {
    private final TaskService taskService;

    private static final ConcurrentHashMap<Long, WebSocketSession> loginUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("************ task deletion web socket connected ************");

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("************ task deletion occurred ************");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("************ task deletion web socket connection closed ************");
    }

    private Long getUserId(WebSocketSession session){
        return ((LoginUser) session.getAttributes().get("loginUser")).getUserId();
    }
}
