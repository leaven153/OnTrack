package me.jhchoi.ontrack.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/{page}")
    public String errorPage(@PathVariable String page){
        log.info("들어옴!: {}", page);
        return "/error/" + page;
    }
}
