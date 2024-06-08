package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.TaskFormRequest;
import me.jhchoi.ontrack.dto.LoginUser;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class HomeController {
    
    // ModelAttribute: 중요도(radio), 진행상태(radio), 프로젝트팀원목록(multi checkbox)
    // 유저, 프로젝트, 멤버 C 먼저 해야 할 듯
    /**
     * 유저, 멤버: 회원가입 생략 하고 repository test에서 intstream으로 입력
     * */
//    @GetMapping("/project")
    public String working(Model model){
        log.info("================from home to project directly====================");
        TaskFormRequest addTask = TaskFormRequest.builder()
                .projectId(1L)
                .taskAuthorMid(1L)
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

    /**
     * created  : 24-05-
     * param    :
     * return   :
     * explain  : 로그인 페이지로 이동
     * */
    @GetMapping("login")
    public String login(Model model){
        model.addAttribute("loginRequest", new LoginUser());
        return "login/login";
    }
}
