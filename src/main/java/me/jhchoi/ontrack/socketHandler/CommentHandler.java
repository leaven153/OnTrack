package me.jhchoi.ontrack.socketHandler;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.LoginUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CommentHandler extends TextWebSocketHandler {

    // 로그인한 전체
//    List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    private static final ConcurrentHashMap<Long, WebSocketSession> loginUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        log.info("after Connection established: {}", session);
        // WebSocketServerSockJsSession[id=utvotjvp]
        log.info("session get attribute: {}", session.getAttributes());
        // session get attribute: {} hadnshaker가 없었기 때문!
        // session get attribute: {loginUser=LoginUser(loginId=user@abc.com, loginPw=null, userName=공지철, userId=35)
        // , HTTP.SESSION.ID=B2F8EA55C23D6C16AEA4925D2DE7D5E5}
        loginUsers.put(getUserId(session), session);
//        log.info("로그인한 유저를 세션에 추가: {}", session.getId());
//        try {
//
//        } catch(Exception e){
//            log.info("유저를 세션에 추가할 수 없습니다.: {}", e.getMessage());
//        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String rawData = message.getPayload();
        log.info("handleTextMessage가 받는 session: {}", session.getAttributes());
        // handleTextMessage가 받는 session: {loginUser=LoginUser(loginId=user@abc.com, loginPw=null, userName=공지철, userId=35), HTTP.SESSION.ID=B2F8EA55C23D6C16AEA4925D2DE7D5E5}
        log.info("클라이언트가 보내온 data: {}", rawData);
        log.info("현재 로그인된 유저: {}", loginUsers.get(getUserId(session)));
        loginUsers.get(getUserId(session)).sendMessage(new TextMessage(rawData));
        // 클라이언트가 보내온 data: [object Object]
        // 클라이언트가 보내온 data: 43string
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(getUserId(session) != null){
            loginUsers.remove(getUserId(session));
        }
    }

    private Long getUserId(WebSocketSession session){
        log.info("getUserId 실행(session값): {}", session.getAttributes());
//        Map<String, Object> httpSession = session.getAttributes();
        log.info("아하: {}", session.getAttributes().get("loginUser"));
        // LoginUser(loginId=user@abc.com, loginPw=null, userName=공지철, userId=35)

        LoginUser userInfo = (LoginUser) session.getAttributes().get("loginUser");
        log.info("그렇다면, : {}", userInfo);
        log.info("userId: {}", userInfo.getUserId());

        Long userId = userInfo.getUserId();
        log.info("최종: {}", userId);
        return userId;
    }

}
