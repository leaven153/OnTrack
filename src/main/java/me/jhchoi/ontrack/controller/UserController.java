package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.NewUser;
import me.jhchoi.ontrack.dto.ResponseInvitation;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.service.ProjectService;
import me.jhchoi.ontrack.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    /**
     * created  : 24-07-08
     * param    :
     * return   :
     * explain  : 회원가입 절차 1/2(메일 인증)
     * */
    @PostMapping("/signup/step1")
    public ResponseEntity<?> signupStep1(@RequestBody String email){
        log.info("회원가입 신청(email): {}", email);
//        return userService.signUp(email);
        return null;
    }

    /**
     * created  : 24-07-08
     * param    :
     * return   :
     * explain  : 회원가입 절차 2/2(인증링크 접속, 비밀번호, 이름 입력)
     * */
    @GetMapping("/signup/step2")
    public String signUpLink(@RequestParam String vCode, Model model){
        // 메일의 링크를 통해 들어옴!: 881ac812-8fa4-48af-b9a0-60dafb77a9c5
        log.info("메일의 링크를 통해 들어옴!: {}", vCode);

        // vCode와 일치하는 user email 확인하여 값 넣는다.
//        NewUser user = NewUser.builder().build();
        // 링크
//        model.addAttribute("newUser", user);

        return "/signup/signup_step3";
    }

    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 로그인 실행
     * */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginUser loginRequest, BindingResult bindingResult, HttpServletRequest request, Model model){
        log.info("로그인 입력정보: {}", loginRequest); // 로그인 입력정보: LoginUser(loginId=user1@abc.com, loginPw=admin1234)

        if(loginRequest == null) {
            model.addAttribute("loginRequest", LoginUser.builder().build());
            return "/login/login";
        }
        if(Objects.equals(loginRequest.getLoginId(), "") || loginRequest.getLoginId() == null) {
            log.info("id 입력 안했을 때 if 들어옴");
            bindingResult.rejectValue("loginId", "required", "error입니다");
        }

        if(bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            model.addAttribute("loginRequest", LoginUser.builder().build());
            return "login/login";
        }
        LoginUser loginUser = userService.login(loginRequest.getLoginId(), loginRequest.getLoginPw());
        if(loginUser == null) {
            model.addAttribute("loginRequest", LoginUser.builder().build());
            return "login/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);
        log.info("session 생성: {}", session.getAttribute("loginUser"));
        return "redirect:/mypage/myProjects";
    }

    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 조인(회원의 초대 수락)
     * */
    // 유저가 프로젝트 초대를 수락해야 멤버가 된다.
    // 초대 수락/거절은 user controller가 받는다.
    // 초대 수락은 곧 '내 프로젝트 목록에 추가'의 의미이기 때문에 AddProjectRequest를 사용하자.
    // session 정보와 파라미터로 받은 project id를 addProjectRequest(프로젝트 추가)의 member()로 담아
    // project 서비스에 넘긴다.
    @GetMapping("/join")
    public String acceptInvitation(HttpSession session, @RequestParam("projectType") String projectType, @RequestParam("projectId") Long projectId, @RequestParam("invitedAs") String invitedAs){
        log.info("========== 프로젝트 수락 버튼 눌러서 controller 진입 ==========");
        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        ResponseInvitation newCrew = ResponseInvitation.builder()
                .projectId(projectId)
                .userId(user.getUserId())
                .userName(user.getUserName())
                .invitedAs(invitedAs)
                .capacity(ResponseInvitation.tellCapacity(projectType, invitedAs))
                .joinedAt(LocalDate.now())
                .build();
        projectService.acceptInvitation(newCrew);
        return "redirect:/mypage/myProjects";
    }

    /**
     * created : 2024-06-19
     * param   : HttpSession
     * return  : login
     * explain : 로그아웃
     * */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
