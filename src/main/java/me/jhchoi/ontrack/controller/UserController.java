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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
     * created  : 24-07-08
     * param    :
     * return   :
     * explain  : 회원가입 절차 1/3(인증코드 생성 후, 메일 전송)
     * */
    @PostMapping("/signup/step1")
    public ResponseEntity<?> signupSendMail(@RequestBody NewUser newUser){
        log.info("회원가입 신청(email): {}", newUser);
        return userService.sendVerificationMail(newUser);
//        return null;
    }

    /**
     * created  : 24-07-08
     * param    :
     * return   :
     * explain  : 회원가입 절차 2/2(인증링크)
     * */
    @GetMapping("/signup/step2")
    public String signUpLinkVerify(@RequestParam(required = false) String vCode, Model model){
        // 메일의 링크를 통해 들어옴!: 881ac812-8fa4-48af-b9a0-60dafb77a9c5 (메리포핀스: users1@abc.com, id=38)
        log.info("인증 링크 통해 들어옴!: {}", vCode);

        // vCode 없이 url 접근 시 에러페이지로 이동
        if(vCode == null) {
            model.addAttribute("errorMsg", "잘못된 코드입니다.");
            return "/error/error-verify-signup";
//            redirectAttributes.addFlashAttribute("errorMsg", "잘못된 코드입니다.");
//            return new RedirectView("/error/error-verify-signup");
        }

        ResponseEntity<?> result = userService.signUpVerifyLink(vCode);
        log.info("ResponseEntity확인: {}", result);
        log.info("ResponseEntity 안의 객체는 어떻게 접근?: {}", result.getBody());
        // ResponseEntity 안의 객체는 어떻게 접근?:
        // NewUser(userEmail=users1@abc.com,
        // password=$2a$10$5OiektMZnzXs6oei6SRwGewTaWcsm2Rgh9CTb2pxOKmY5ZpEubZGW,
        // userName=메리 포핀스, verificationCode=881ac812-8fa4-48af-b9a0-60dafb77a9c5, verified=true)

        // 해당 인증코드를 가진 유저가 없거나 이미 인증을 마친 유저라면 에러 페이지로 이동
        if(result.getStatusCode().is4xxClientError() || result.getStatusCode().is5xxServerError()){
            log.info("해당 인증코드를 가진 유저가 없거나 이미 인증을 마친 유저의 에러페이지로 이동필요: {}", result.getBody());
//            redirectAttributes.addFlashAttribute("errorMsg", result.getBody());
//            return new RedirectView("/error/error-verify-signup");
            model.addAttribute("errorMsg", result.getBody());
            return "/error/error-verify-signup";
        }


        model.addAttribute("newUser", LoginUser.builder().loginId((String) result.getBody()).build());
        return "signup/signup_step3";

        // redirectView는 Get으로만 Mapping 된다...
//        redirectAttributes.addFlashAttribute("loginRequest", result.getBody());
//        return new RedirectView("/login"); // /mypage/myProjects
    }



    /**
     * created  : 24-05-
     * param    :
     * return   :
     * explain  : 로그인 페이지로 이동
     * */
    @GetMapping("/login")
    public String loginForm(Model model){
        log.info("로그인폼요청");
        model.addAttribute("loginRequest", new LoginUser());
        return "login/login";
    }
    
    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 로그인 실행
     * */
    @PostMapping("/login")
    public RedirectView login(@ModelAttribute LoginUser loginRequest, HttpServletRequest request, RedirectAttributes redirectAttributes){
        log.info("로그인 입력정보: {}", loginRequest); // 로그인 입력정보: LoginUser(loginId=user1@abc.com, loginPw=admin1234)

        // 이렇게 유효성 검사를 해도 됐겠네?!
//        if(loginRequest.getLoginId().isEmpty() || loginRequest.getLoginPw().isEmpty()) {
//            return "redirect:/login";
//        }

        ResponseEntity<?> result = userService.login(loginRequest.getLoginId(), loginRequest.getLoginPw());
        // 해당 유저의 비번이 매칭되지 않았을 때
        if(result.getStatusCode().is4xxClientError()) {
            redirectAttributes.addFlashAttribute("noMatch", result.getBody());
            return new RedirectView("/login");
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", result.getBody());
        log.info("session 생성: {}", session.getAttribute("loginUser"));
        return new RedirectView("/mypage/myProjects");
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
