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
import java.util.Optional;

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
     * explain  : 회원가입 절차 1/3(인증코드 생성 후, 메일 전송)
     * */
    @PostMapping("/signup/step1")
    public ResponseEntity<?> signupSendMail(@RequestBody String email){
        log.info("회원가입 신청(email): {}", email);
        return userService.sendVerificationMail(email);
//        return null;
    }

    /**
     * created  : 24-07-08
     * param    :
     * return   :
     * explain  : 회원가입 절차 2/3(인증링크)
     * */
    @GetMapping("/signup/step2")
    public String signUpLinkVerify(@RequestParam(required = false) String vCode, Model model){
        // 메일의 링크를 통해 들어옴!: 881ac812-8fa4-48af-b9a0-60dafb77a9c5 (메리포핀스: users1@abc.com, id=38)
        log.info("인증 링크 통해 들어옴!: {}", vCode);

        // vCode 없이 url 접근 시 에러페이지로 이동
        if(vCode == null) {
            return "/error/novcode";
        }

        // 해당 인증코드를 가진 유저가 없다면 에러 페이지로 이동
        Optional<NewUser> nUser = userService.signUpVerifyLink(vCode);
        if(nUser.isEmpty()){
            return "/error/novcode";
        }

        // 이미 verified 되어 있다면 에러 페이지로 이동(login페이지 링크된 에러 페이지)
        if(!nUser.get().getVerified()){
            return "/error/alreadyverified";
        }

        model.addAttribute("newUser", nUser);

        return "/signup/signup_step3";
    }

    /**
     * created  : 24-07-09
     * param    :
     * return   :
     * explain  : 회원가입 절차 3/3(비밀번호  입력)
     * */
    @PostMapping("/signup/step3")
    public String signUpPw(@RequestBody NewUser newUser){
        
        log.info("가입 절차 마지막 단계: {}", newUser);

        // 비밀번호 암호화하여 DB에 저장한 후,

        // userEmail과 loginPw를 LoginUser객체에 담아 model에 add 후, login으로 넘긴다.

        return "redirect:/login";
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
