package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.service.ProjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.view.ThymeleafView;

import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;


    @ModelAttribute("statusMap")
    public Map<Integer, String[]> taskStatusModel(){
        Map<Integer, String[]> statusMap = new LinkedHashMap<>();
        statusMap.put(0, new String[]{"보류", "pause", "pause-bg", "pause-border-shadow"});
        statusMap.put(1, new String[]{"시작 안 함", "not-yet", "notYet-bg20", "notYet-border-shadow"});
        statusMap.put(2, new String[]{"계획중", "planning", "planning-bg008", "planning-border-shadow"});
        statusMap.put(3, new String[]{"진행중", "ing", "ing-bg008", "ing-border-shadow"});
        statusMap.put(4, new String[]{"검토중", "review", "review-bg008", "review-border-shadow"});
        statusMap.put(5, new String[]{"완료", "done", "done-bg008", "done-border-shadow"});
        return statusMap;
    }

/*
    // 할 일 상세 fragment만 rendering
    @Bean(name="statusView")
    @Scope("prototype")
    public ThymeleafView statusViewBean(){
        ThymeleafView statusView = new ThymeleafView("projectView");
        statusView.setMarkupSelector("#status-view");
        log.info("이게 언제 불려지나? === fragment bean"); // WebConfig에 있어도, 현 컨트롤러 안에 있어도 2번 출력된다.
        return statusView;
    }
*/
    /**
     * created  : 2024-05-
     * updated  : 2024-05-23
     * param    :
     * return   :
     * explain  : 개별 프로젝트 진입(프로젝트 할 일 목록 조회)
     * */
    @GetMapping("/{projectId}")
    public String getProject(@PathVariable Long projectId, @RequestParam(required = false) String view, HttpSession session, Model model){
        log.info("==============================개별 프로젝트 controller 진입==============================");
        log.info("project id: {}", projectId);
        log.info("선택한 view: {}", view);
//        log.info("path variable member id: {}", memberId); // 추후

        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            URI location = URI.create("/login");
//            return ResponseEntity.created(location).build();
            return "redirect:/login";
        }

        // 2. project
        // 2-1. 프로젝트 정보 - OnTrackProject(프로젝트명, 생성자, 생성일, 유형, 마감일, 상태)
        // 2-2. 해당 프로젝트의 멤버들: List<MemberInfo>
        // 2-3. 할 일 목록 - List<TaskList>
        // 2-4. 담당자별 할 일 목록 - List<AssignmentList>
        ProjectResponse project = projectService.getProject(projectId, loginUser.getUserId());
        model.addAttribute("project", project);


        // 3. 마감일 D-Day 계산을 위한 오늘 날짜
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        // 4. 할일 추가 객체(TaskFormRequest)
        model.addAttribute("taskFormRequest", TaskFormRequest.builder().projectId(projectId).taskAuthorMid(project.getMemberId()).build());

        URI projectLocation = URI.create("project/project");

        // null일 경우, thymeleaf가 table view로 처리한다.
        if(view != null){
            model.addAttribute("view", view);
//            return ResponseEntity.created(projectLocation).body(view);
        }

//        return ResponseEntity.created(projectLocation).build();
        return "project/project"; // url - http://localhost:8080/project/9
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
    public String createProjectSubmit(@ModelAttribute AddProjectRequest newProjectRequest, HttpSession session){
        log.info("======= 새 프로젝트 등록 =========");
        log.info("프로젝트 폼: {}", newProjectRequest);
        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        newProjectRequest.setCreator(user.getUserId());
        ProjectMember newCreator = AddProjectRequest.creator(user.getUserId(), user.getUserName());
        projectService.createProject(newProjectRequest.toProjectEntity(), newCreator);
        log.info("프로젝트 엔티티생성확인: {}", newProjectRequest.toProjectEntity());
        log.info("멤버 엔티티생성확인: {}", newCreator);
        return "redirect:/mypage/myProjects";
    }









}
