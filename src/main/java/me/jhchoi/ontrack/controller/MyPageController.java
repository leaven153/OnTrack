package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/mypage")
public class MyPageController {
    @GetMapping("/myProjects")
    public String myProjects(HttpSession session, HttpServletRequest request){
        log.info("=================myProjects====================");

        log.info("session을 찾아라: {}", session.getAttribute("loginUser"));
        return "/mypage/myProjects";
    }

    @GetMapping("/myTasks")
    public String myTasks(){
        log.info("=================myTasks====================");
        return "/mypage/myTasks";
    }

    @GetMapping("/bin")
    public String myBin(){
        log.info("=================bin====================");
        return "/mypage/bin";
    }
}
