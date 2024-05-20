package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.LoginRequest;
import me.jhchoi.ontrack.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 로그인 실행
     * */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpServletRequest request){
        log.info("로그인 입력정보: {}", loginRequest); // 로그인 입력정보: LoginRequest(loginId=user1@abc.com, loginPw=admin1234)

        if(Objects.equals(loginRequest.getLoginId(), "") || loginRequest.getLoginId() == null) {
            log.info("id 입력 안했을 때 if 들어옴");
            bindingResult.rejectValue("loginId", "required", "error입니다");
        }

        if(bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);

            return "redirect:login/login";
        }
        LoginRequest loginUser = userService.login(loginRequest.getLoginId(), loginRequest.getLoginPw());

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);
        log.info("session 생성: {}", session.getAttribute("loginUser"));


        return "redirect:/mypage/myProjects";
    }

}
