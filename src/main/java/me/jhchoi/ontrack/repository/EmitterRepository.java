package me.jhchoi.ontrack.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter){
        emitters.put(userId, sseEmitter);
        return sseEmitter;
    }

    public void deleteById(Long userId){
        emitters.remove(userId);
    }
}
