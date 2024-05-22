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
        model.addAttribute("addProjectRequest", new AddProjectRequest());
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        List<ProjectList> projectList = projectService.allMyProjects(loginUser.getUserId());
        model.addAttribute("projectList", projectList);
        model.addAttribute("loginUser", loginUser);
        log.info("컨트롤러가 화면으로 넘기는 project list: {}", projectList);
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
