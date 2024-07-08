package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.NewUser;
import me.jhchoi.ontrack.dto.LoginUser;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class HomeController {
    

    /**
     * created  : 24-05
     * param    : void
     * return   : 회원 가입 페이지
     * explain  : 첫 화면에서 회원가입페이지로 이동
     * */
    @GetMapping("/signup")
    public String signUp(){
        log.info("================go to sign up===================");
//        model.addAttribute("addUser", new NewUser());
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
