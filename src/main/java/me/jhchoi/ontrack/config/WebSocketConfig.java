package me.jhchoi.ontrack.config;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.socketHandler.CommentHandler;
import me.jhchoi.ontrack.socketHandler.TaskDeletion;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket @RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final CommentHandler commentHandler;
    private final TaskDeletion taskDeletion;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(commentHandler, "/ws/noticeComment")
                .addHandler(taskDeletion, "/ws/taskDeletion")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:8080").withSockJS();
    }
}
