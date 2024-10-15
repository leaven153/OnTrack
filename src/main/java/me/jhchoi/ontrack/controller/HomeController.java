package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.LoginUser;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    // 둘러보기
    @GetMapping("/guest")
    public String lookAroundGuest(HttpServletRequest request){
        log.info("********************* GUEST ENTER ***********************");
        // UserService의 login(id, pw)를 여기서 바로 소환할 것인가..
        // 둘러보기를 동시에 요청할 경우는 어떻게 대처하려고?
//        HttpSession session = request.getSession();
//        session.setAttribute("loginUser", "Thor");
        return "/mypage/myProjects";
    }
    // 회원가입과 로그인은 UserController로 이동(24/7/11)
}
