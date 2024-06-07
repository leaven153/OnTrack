package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.AddProjectRequest;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.dto.ProjectList;
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

        log.info("session을 찾아라: {}", session.getAttribute("loginUser"));
        // AddProjectRequest의 creator에 로그인한 유저의 id, nickname 담는 코드 추가요망
        model.addAttribute("createProjectRequest", new AddProjectRequest());
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login/login";
        }
        List<ProjectList> projectList = projectService.allMyProjects(loginUser.getUserId());
        Boolean noProject = projectList.size() == 0; // Boolean noProject = projectList.size() <= 0 ? true: false;

//        log.info("projectList가 0개라면 null인가?: {}", projectList); //projectList가 0개라면 null인가?: []
//        log.info("projectList.size: {}", projectList.size()); // projectList.size: 2
//        log.info("noProject: {}", noProject); // noProject: false

        model.addAttribute("noProject", noProject);
        model.addAttribute("projectList", projectList);
        model.addAttribute("loginUser", loginUser);
        log.info("컨트롤러가 화면으로 넘기는 project list: {}", projectList);
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
        return "/mypage/bin";
    }
}
