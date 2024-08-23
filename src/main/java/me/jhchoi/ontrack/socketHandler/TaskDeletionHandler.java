package me.jhchoi.ontrack.socketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

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
        log.info("클라이언트가 보내온 data: {}", receivedData); // 클라이언트가 보내온 data: 15,16

        // 휴지통으로 옮겨진 할 일의 task id를 배열로 만든다.
        List<Long> taskIdL = new ArrayList<>();
        if (receivedData.contains(",")) {
            String[] taskIds = receivedData.split(",");
            for(int i = 0; i < taskIds.length; i++){
                taskIdL.add(Long.valueOf(taskIds[i]));
            }
        } else {
            taskIdL.add(Long.valueOf(receivedData));
        }

        // 해당 task의 담당자 중 현재 로그인한 유저가 있다면 task id를 전송한다.
        for(Long targetUser : loginUsers.keySet()){
            for(int i = 0; i < taskIdL.size(); i++){
                if(taskService.alarmBin(taskIdL).get(taskIdL.get(i)).stream().anyMatch(Predicate.isEqual(targetUser))){
                    loginUsers.get(targetUser).sendMessage(new TextMessage(taskIdL.get(i).toString()));
                }
            }
        }
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
