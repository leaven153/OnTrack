package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.ResponseInvitation;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.service.ProjectService;
import me.jhchoi.ontrack.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

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
            return "/login/login";
        }
        if(Objects.equals(loginRequest.getLoginId(), "") || loginRequest.getLoginId() == null) {
            log.info("id 입력 안했을 때 if 들어옴");
            bindingResult.rejectValue("loginId", "required", "error입니다");
        }

        if(bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "redirect:login/login";
        }
        LoginUser loginUser = userService.login(loginRequest.getLoginId(), loginRequest.getLoginPw());
        if(loginUser == null) {
            return "redirect:login/login";
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

}
