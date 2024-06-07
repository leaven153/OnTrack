package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;


    @ModelAttribute("statusList")
    public Map<Integer, String[]> taskStatusModel(){
        Map<Integer, String[]> statusList = new LinkedHashMap<>();
        statusList.put(0, new String[]{"시작 안 함", "not-yet", "notYet-bg20", "notYet-border-shadow"});
        statusList.put(1, new String[]{"계획중", "planning", "planning-bg008", "planning-border-shadow"});
        statusList.put(2, new String[]{"진행중", "ing", "ing-bg008", "ing-border-shadow"});
        statusList.put(3, new String[]{"검토중", "review", "review-bg008", "review-border-shadow"});
        statusList.put(4, new String[]{"완료", "done", "done-bg008", "done-border-shadow"});
        return statusList;
    }

    /**
     * created  : 2024-05-
     * updated  : 2024-05-23
     * param    :
     * return   :
     * explain  : 개별 프로젝트 진입(프로젝트 할 일 목록 조회)
     * */
    @GetMapping("/{projectId}/{memberId}/{nickname}/{position}")
    public String getProject(@PathVariable("projectId") Long projectId, @PathVariable("memberId") Long memberId, @PathVariable("nickname")String nickname, @PathVariable("position")String position, HttpSession session, Model model){
        log.info("==============================개별 프로젝트 controller 진입==============================");
        log.info("path variable project id: {}", projectId);
        log.info("path variable member id: {}", memberId); // 추후

        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "login/login";
        }

        // 1. 해당 프로젝트에 접근한 멤버의 정보
        model.addAttribute("loginMember", MemberList.builder().userId(loginUser.getUserId()).projectId(projectId).memberId(memberId).nickName(nickname).position(position).build());
        // 1. member의 nickname 매칭 (project list에서 pathvariable로 넘긴 값)
//        model.addAttribute("nickname", nickname);

        // 2. project
        // 2-1. 프로젝트 정보 - OnTrackProject(프로젝트명, 생성자, 생성일, 유형, 마감일, 상태)
        // 2-2. 해당 프로젝트의 멤버들: List<MemberList>
        // 2-3. 할 일 목록 - List<TaskList>
        // 2-4. 담당자별 할 일 목록 - List<AssignmentList>
        ProjectResponse project = projectService.getProject(projectId);
//        log.info("프로젝트로 넘어가는 값 중 최신 task: {}", project.getTaskList().get(project.getTaskList().size()-1));
        model.addAttribute("project", project);


        // 3. 마감일 D-Day 계산을 위한 오늘 날짜
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        // 4. 할일 추가 객체(AddTaskRequest)
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
