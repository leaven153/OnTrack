package me.jhchoi.ontrack.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.AddTaskRequest;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;


    @PostMapping("/addTask")
    public void addTaskSubmit(@ModelAttribute AddTaskRequest addTaskRequest){
        System.out.println("please");
        log.info("=============from 할일추가 form==================");
        log.info("프로젝트아이디 = {}", addTaskRequest.getProjectId());
        log.info("작성자아이디 = {}", addTaskRequest.getTaskAuthor());
        log.info("할일 이름 = {}", addTaskRequest.getTaskTitle());
        log.info("전체 = {}", addTaskRequest.toString());
//        taskService.addTask(addTaskRequest);
//        return null;
    }

}
