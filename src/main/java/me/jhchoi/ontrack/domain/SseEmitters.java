package me.jhchoi.ontrack.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitters {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<Long, SseEmitter> userEmitter = new ConcurrentHashMap<>();
    private final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;

    public SseEmitter add(SseEmitter emitter){
        this.emitters.add(emitter);
        log.info("새 emitter 더해짐: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion 불려짐");
            this.emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeOut 불려짐");
            emitter.complete();
        });

        return emitter;
    } // add ends


    public SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        this.userEmitter.put(userId, emitter);
        log.info("emitter 생성: {}", userEmitter);
//        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> {
            this.userEmitter.remove(userId);
        });
        emitter.onTimeout(emitter::complete);
//        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
//        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }

    public void deleteById(Long userId){
        userEmitter.remove(userId);
    }

    public SseEmitter get(Long userId){
        return userEmitter.get(userId);
    }

    public void sendNotification(Long userId, String event, Object data){
        SseEmitter emitter = userEmitter.get(userId);
        log.info("sendNotification이 불려졌다: {}", emitter);
        if(emitter != null){
            try{

                emitter.send(SseEmitter.event().name(event).data(data));
            } catch(IOException e){
                log.info("해당 유저의 emitter가 없습니다.", e.getMessage());
                userEmitter.remove(userId);
            }
        }
    }


}
