package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.dto.MyProject;
import me.jhchoi.ontrack.dto.ProjectRequest;
import me.jhchoi.ontrack.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final ProjectService projectService;

    @GetMapping("/myProjects")
    public String myProjects(HttpSession session, Model model){

        log.info("=================myProjects====================");
        // AddProjectRequest의 creator에 로그인한 유저의 id, nickname 담는 코드 추가요망
        model.addAttribute("createProjectRequest", new ProjectRequest());
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            log.info("로그인 정보 없음");
            return "redirect:../login";
        }
        log.info("session: {}", loginUser);
        List<MyProject> myProjects = projectService.allMyProjects(loginUser.getUserId());
        Boolean noProject = myProjects.isEmpty(); // Boolean noProject = projectList.size() <= 0 ? true: false;

//        log.info("projectList가 0개라면 null인가?: {}", projectList); //projectList가 0개라면 null인가?: []
//        log.info("projectList.size: {}", projectList.size()); // projectList.size: 2
//        log.info("noProject: {}", noProject); // noProject: false

        model.addAttribute("noProject", noProject);
        model.addAttribute("myProjects", myProjects);
        model.addAttribute("loginUser", loginUser);
        log.info("컨트롤러가 화면으로 넘기는 project list: {}", myProjects);
        // 컨트롤러에서 넘어가는 시점: 2024-06-04T17:24:33.630980400
        return "mypage/myProjects";
    }


    @GetMapping("/myTasks")
    public String myTasks(){
        log.info("=================myTasks====================");
        return "/mypage/myTasks";
    }

    @GetMapping("/bin")
    public String myBin(){
        log.info("=================bin====================");
        // deletedAt이 7일 지난 것은 출력하지 않는다.

        return "/mypage/bin";
    }
}
