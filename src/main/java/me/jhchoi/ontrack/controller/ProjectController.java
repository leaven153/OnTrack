package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectController {

    @GetMapping("/project")
    public String project(){
        log.info("===============project================");
        return "/project/project";
    }
}
