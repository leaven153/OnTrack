package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/project")
    public String working(){
        log.info("================from home to project directly====================");
//        System.out.println("please...");
        return "project/project";
    }

    @GetMapping("/signup")
    public String signUp(){
        log.info("================sign up===================");
        return "signup/signup";
    }
}
