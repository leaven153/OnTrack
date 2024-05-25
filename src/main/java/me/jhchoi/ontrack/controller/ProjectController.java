package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
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
     * created  : 2024-05-
     * updated  : 2024-05-23
     * param    :
     * return   :
     * explain  : 개별 프로젝트 진입(프로젝트 할 일 목록 조회)
     * */
    @GetMapping("/{projectId}/{memberId}/{nickname}")
    public String eachProject(@PathVariable("projectId") Long projectId, @PathVariable("memberId") Long memberId, @PathVariable("nickname")String nickname, HttpSession session, Model model){
        log.info("==============================개별 프로젝트 controller 진입==============================");
        log.info("path variable project id: {}", projectId);
        log.info("path variable member id: {}", memberId); // 추후 이것도 uuid 처리해야 할까?

        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");

        // 1. member의 nickname 매칭 (project list에서 pathvariable로 넘긴 값)
        model.addAttribute("nickname", nickname);

        // 2. project 정보


        // 3. 할 일 목록 (projectId)
        
        // 4. 해당 프로젝트의 멤버들: MemberNickNames에 담아온다.
        
        // 5. 할일 추가 객체(AddTaskRequest) 넘기기
        model.addAttribute("addTaskRequest", AddTaskRequest.builder().projectId(projectId).taskAuthorMid(memberId).build());

        return "project/project"; // url - http://localhost:8080/project/9/14
    }


    /**
     * created : 2024-05-21
     * param   :
     * return  :
     * explain : 프로젝트 생성(team)
     * 프로젝트를 생성하는 것은 mypage에서만 가능하도록 수정하면
     * modelAttribute어노테이션을 이용해 프로젝트 멤버를 항상 붙일 수 있을 듯? →
     * */
    @PostMapping("/createProject")
    public String createProjectSubmit(@ModelAttribute AddProjectRequest newProjectRequest, BindingResult bindingResult, HttpSession session){
        log.info("======= 새 프로젝트 등록 =========");
        log.info("프로젝트 폼: {}", newProjectRequest);
        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        newProjectRequest.setCreator(user.getUserId());
        ProjectMember newCreator = AddProjectRequest.creator(user.getUserName());
        projectService.createProject(newProjectRequest.toProjectEntity(), newCreator);
        log.info("프로젝트 엔티티생성확인: {}", newProjectRequest.toProjectEntity());
        log.info("멤버 엔티티생성확인: {}", newCreator);
        return "redirect:/mypage/myProjects";
    }









}
