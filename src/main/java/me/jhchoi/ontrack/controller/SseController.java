package me.jhchoi.ontrack.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.CheckComment;
import me.jhchoi.ontrack.domain.CheckNotice;
import me.jhchoi.ontrack.domain.SseEmitters;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SseController {

    private final SseEmitters sseEmitters;
    private final TaskService taskService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(){
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try{
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e){
            log.info("sse error: {}", e.getMessage());
        }
        return ResponseEntity.ok(emitter);
    }

    @GetMapping(value="/commentListener", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> noticeCommentListener(){

        Map<Long, Long> taskAndCommentId = new HashMap<>();
        taskAndCommentId.put(22L, 24L);
        CheckComment cc = CheckComment.builder().commentId(24L).memberId(9L).build();
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try{
            emitter.send(SseEmitter.event()
                    .name("noticeComment")
                    .data(cc));
        } catch (IOException e){
            log.info("sse error: {}", e.getMessage());
        }
        return ResponseEntity.ok(emitter);
    }
}
