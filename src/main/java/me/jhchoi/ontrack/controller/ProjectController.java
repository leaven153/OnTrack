package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.AddProjectRequest;
import me.jhchoi.ontrack.dto.AddTaskRequest;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;


    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 內 할 일 목록 조회
     * */
    @GetMapping("/{projectId}/{memberId}")
    public String eachProject(@PathVariable("projectId") Long projectId, @PathVariable("memberId") Long memberId, HttpSession session, Model model){
        log.info("==============================개별 프로젝트 controller 진입==============================");
        log.info("path variable project id: {}", projectId);
        log.info("path variable member id: {}", memberId);
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        // 할 일 추가 form 객체에 아래 정보 담아서 넘기기
        // 해당 프로젝트의 project id, 
        // author에  user id 담..기?
        // 해당 프로젝트의 멤버들: MemberNickNames에 담아온다.
        model.addAttribute("addTaskRequest", new AddTaskRequest());



        return "/project/project";
    }


    /**
     * created : 2024-05-21
     * param   :
     * return  :
     * explain : 프로젝트 생성(team)
     * */
    @PostMapping("/addProject")
    public String addProjectSubmit(@ModelAttribute AddProjectRequest addProjectRequest, BindingResult bindingResult, HttpSession session){
        log.info("======= 새 프로젝트 등록 =========");
        log.info("프로젝트 폼: {}", addProjectRequest);
        LoginUser creator = (LoginUser) session.getAttribute("loginUser");
        addProjectRequest.setCreator(creator.getUserId());
        addProjectRequest.setNickname(creator.getUserName());
        addProjectRequest.setPosition("creator");
        addProjectRequest.setCapacity("PD");
        projectService.createProject(addProjectRequest.toProjectEntity(), addProjectRequest.toPMEntity());
        log.info("프로젝트 엔티티생성확인: {}", addProjectRequest.toProjectEntity());
        log.info("멤버 엔티티생성확인: {}", addProjectRequest.toPMEntity());
        return "redirect:/mypage/myProjects";
    }


    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 초대
     * */



}
