package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/mypage")
public class NavController {
    @GetMapping("/myProjects")
    public String myProjects(){
        log.info("=================myProjects====================");
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
