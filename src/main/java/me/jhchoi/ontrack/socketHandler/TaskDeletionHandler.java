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
public class TaskDeletionHandler extends TextWebSocketHandler {
    private final TaskService taskService;

    private static final ConcurrentHashMap<Long, WebSocketSession> loginUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("************ 할 일 지우기 소켓 연결 ************");
        loginUsers.put(getUserId(session), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("************ 할 일 지워짐 task deletion occurred ************");
        String receivedData = message.getPayload(); // .split(",");
        log.info("클라이언트가 보내온 data: {}", receivedData);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(getUserId(session) != null){
            log.info("************ 할 일 지우기 소켓 연결 끊김task deletion web socket connection closed ************");
            loginUsers.remove(getUserId(session));
        }
    }

    private Long getUserId(WebSocketSession session){
        return ((LoginUser) session.getAttributes().get("loginUser")).getUserId();
    }
}
