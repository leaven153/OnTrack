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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Slf4j
@Component @RequiredArgsConstructor
public class CommentHandler extends TextWebSocketHandler {

    private final TaskService taskService;
    // 로그인한 전체
//    List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    private static final ConcurrentHashMap<Long, WebSocketSession> loginUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        log.info("********after Connection established ***********");
        log.info("after Connection established: {}", session);
        // WebSocketServerSockJsSession[id=utvotjvp]
//        log.info("session get attribute: {}", session.getAttributes());
        // session get attribute: {} hadnshaker가 없었기 때문!
        // session get attribute: {loginUser=LoginUser(loginId=user@abc.com, loginPw=null, userName=공지철, userId=35)
        // , HTTP.SESSION.ID=B2F8EA55C23D6C16AEA4925D2DE7D5E5}
        loginUsers.put(getUserId(session), session);
        log.info("같은 공지철이 어떻게 저장되는가: {}", loginUsers);
//        try {
//
//        } catch(Exception e){
//            log.info("유저를 세션에 추가할 수 없습니다.: {}", e.getMessage());
//        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String receivedData = message.getPayload(); // .split(",");

        log.info("handleTextMessage가 받는 session: {}", session.getAttributes());
        // handleTextMessage가 받는 session: {loginUser=LoginUser(loginId=user@abc.com, loginPw=null, userName=공지철, userId=35), HTTP.SESSION.ID=B2F8EA55C23D6C16AEA4925D2DE7D5E5}
        log.info("클라이언트가 보내온 data: {}", receivedData);
        // 클라이언트가 보내온 data: [object Object] // map을 보내보았음.
        // 클라이언트가 보내온 data: 43string

        // 해당 notice comment를 확인하지 않은 user list(List<CheckComment>) 받아온다.
        Map<Long, List<Long>> taskIdAndUserList = taskService.alarmNoticeComment(Long.valueOf(receivedData));
        List<Long> unchekcdUserList = new ArrayList<>();
        for(List<Long> userList: taskIdAndUserList.values()){
            unchekcdUserList = userList;
        }

        // 현재 loginUser에 해당 user id가 있다면 sendMessage (task id)
        for(Long targetUser: loginUsers.keySet()){
            if(unchekcdUserList.stream().anyMatch(Predicate.isEqual(targetUser))){
                loginUsers.get(targetUser).sendMessage(new TextMessage(taskIdAndUserList.keySet().toArray()[0].toString()));
            }
        }
// Closing session due to exception for WebSocketServerSockJsSession[id=ap5kqgwf]
        //                String data = (String) taskIdAndUserList.keySet().toArray()[0] + "," + targetUser;
//                log.info("taskid와 userid 합침: {}", data);
        //        List<Long> sendAlarmTo = new ArrayList<>();
//        log.info("현재 로그인된 유저: {}", loginUsers.get(getUserId(session)));
//        sendAlarmTo.addAll(loginUsers.keySet());
//        loginUsers.get(getUserId(session)).sendMessage(new TextMessage(rawData));



    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(getUserId(session) != null){
            log.info("********after Connection Closed ***********");
            loginUsers.remove(getUserId(session));
        }
    }

    private Long getUserId(WebSocketSession session){
//        log.info("getUserId 실행(session값): {}", session.getAttributes());
//        Map<String, Object> httpSession = session.getAttributes();
//        log.info("아하: {}", session.getAttributes().get("loginUser"));
        // LoginUser(loginId=user@abc.com, loginPw=null, userName=공지철, userId=35)

        LoginUser userInfo = (LoginUser) session.getAttributes().get("loginUser");
//        log.info("그렇다면, : {}", userInfo);
//        log.info("userId: {}", userInfo.getUserId());

        //        log.info("최종: {}", userId);
        Long userId = userInfo.getUserId();
        return userId;
    }

}
