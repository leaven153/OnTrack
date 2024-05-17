package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.AddTaskRequest;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/project")
    public String working(Model model){
        log.info("================from home to project directly====================");
        AddTaskRequest addTask = AddTaskRequest.builder()
                .projectId(1L)
                .taskAuthor(1L)
                .build();
        model.addAttribute("addTaskRequest", addTask);
        return "project/project";
    }

    /**
     * created  : 24-05
     * param    : void
     * return   : 회원 가입 페이지
     * explain  : 첫 화면에서 회원가입페이지로 이동
     * */
    @GetMapping("/signup")
    public String signUp(){
        log.info("================go to sign up===================");
        return "signup/signup";
    }

    /**
     * created  : 24-05-15
     * param    : OntrackUser,
     * return   :
     * explain  : 회원가입
     * */
    @PostMapping("/signup")
    public String signUpSubmit(){
        log.info("==============submit sign up===================");
        return null;
    }
}
