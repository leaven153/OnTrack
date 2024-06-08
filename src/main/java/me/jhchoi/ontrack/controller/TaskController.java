package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.TaskFormRequest;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;


    @PostMapping("/addTask")
    public RedirectView addTaskSubmit(@ModelAttribute TaskFormRequest taskFormRequest, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            return new RedirectView("login/login");
//            return "redirect:/login/login";
        }
        log.info("=============from 할일추가 form==================");
        log.info("프로젝트아이디 = {}", taskFormRequest.getProjectId());
        log.info("작성자아이디 = {}", taskFormRequest.getTaskAuthorMid());
        log.info("작성자 이름 = {}", taskFormRequest.getAuthorName());
        log.info("할일 이름 = {}", taskFormRequest.getTaskTitle());
        log.info("전체 = {}", taskFormRequest);
//        log.info("파일 = {}", taskFormRequest.getTaskFile().get(0).getOriginalFilename());

        // admin이나 creator가 아닌 member가 생성한 할 일의 중요도는 null값임. 고로, 일반으로 설정하여 service로 넘긴다.
        if (taskFormRequest.getTaskPriority() == null) taskFormRequest.setTaskPriority(2);
        Long newTaskId = taskService.addTask(taskFormRequest);

        String encodedName = URLEncoder.encode(taskFormRequest.getAuthorName(), StandardCharsets.UTF_8);
        log.info("컨트롤러에서 넘어가는 시점: {}", LocalDateTime.now()); // 컨트롤러에서 넘어가는 시점: 2024-06-04T17:50:39.535349900
        // fetch 에서 response 없애고 2024-06-05T21:45:00.923132600

        redirectAttributes.addAttribute("projectId", taskFormRequest.getProjectId());
        redirectAttributes.addAttribute("memberId", taskFormRequest.getTaskAuthorMid());
        redirectAttributes.addAttribute("nickname", taskFormRequest.getAuthorName());

//        return "redirect:/task/test";
        return new RedirectView("/project/{projectId}/{memberId}/{nickname}");

//        return """
//                redirect:/project/%s/%s/%s
//                """.formatted(taskFormRequest.getProjectId(), taskFormRequest.getTaskAuthorMid(), encodedName);
    }


}
