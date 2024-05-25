package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
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
    public String addTaskSubmit(@ModelAttribute AddTaskRequest addTaskRequest, HttpSession session){
        System.out.println("please");
        log.info("=============from 할일추가 form==================");
        log.info("프로젝트아이디 = {}", addTaskRequest.getProjectId());
        log.info("작성자아이디 = {}", addTaskRequest.getTaskAuthorMid());
        log.info("할일 이름 = {}", addTaskRequest.getTaskTitle());
        log.info("전체 = {}", addTaskRequest);
        //전체 = AddTaskRequest(projectId=9, taskAuthorMid=14, taskTitle=할 일 추가 ing, taskPriority=vip, taskDueDate=2024-05-23, assigneesMid=null, nickname=null, taskFile=[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@1b6ce1b0])
//        taskService.addTask(addTaskRequest);
        return """
                redirect:/project/%s/%s
                """.formatted(addTaskRequest.getProjectId(), addTaskRequest.getTaskAuthorMid());
    }

}
