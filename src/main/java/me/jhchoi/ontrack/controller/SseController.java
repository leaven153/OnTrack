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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SseController {

    private final SseEmitters sseEmitters;
    private final TaskService taskService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(){
        // 아래 내용을 로그인 메소드에 포함하도록 해보자.
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

    @GetMapping(value="/commentListener/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> noticeCommentListener(@PathVariable Long userId){

        log.info("유저 아이디: {}", userId);


        Map<Long, Long> taskAndCommentId = new HashMap<>();
        taskAndCommentId.put(22L, 24L);
        List<CheckComment> ccList = new ArrayList<>();
        CheckComment cc = CheckComment.builder().commentId(24L).memberId(9L).userId(47L).build();
        ccList.add(cc);
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try{
            emitter.send(SseEmitter.event()
                    .name("noticeComment")
                    .data(ccList));
        } catch (IOException e){
            log.info("sse error: {}", e.getMessage());
        }
        return ResponseEntity.ok(emitter);
    }
}
