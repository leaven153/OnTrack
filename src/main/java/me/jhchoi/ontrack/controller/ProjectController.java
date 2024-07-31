package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.service.ProjectService;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;


import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;


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


    /**
     * created : 2024-05-21
     * param   :
     * return  :
     * explain : 프로젝트 생성(team)
     * 프로젝트를 생성하는 것은 mypage에서만 가능하도록 수정하면
     * modelAttribute어노테이션을 이용해 프로젝트 멤버를 항상 붙일 수 있을 듯? →
     * */
    @PostMapping("/createProject")
    public String createProjectSubmit(@ModelAttribute ProjectRequest newProjectRequest, HttpSession session){
        log.info("======= 새 프로젝트 등록 =========");
        log.info("프로젝트 폼: {}", newProjectRequest);
        LoginUser user = (LoginUser) session.getAttribute("loginUser");
        newProjectRequest.setCreator(user.getUserId());
        ProjectMember newCreator = ProjectRequest.creator(user.getUserId(), user.getUserName());
        projectService.createProject(newProjectRequest.toProjectEntity(), newCreator);
        log.info("프로젝트 엔티티생성확인: {}", newProjectRequest.toProjectEntity());
        log.info("멤버 엔티티생성확인: {}", newCreator);
        return "redirect:/mypage/myProjects";
    }

    /**
     * created  : 2024-05-
     * updated  : 2024-05-23
     * param    :
     * return   :
     * explain  : 개별 프로젝트 진입(프로젝트 할 일 목록 조회)
     * */
    @GetMapping("/{projectId}")
    public String getProject(@PathVariable Long projectId, @RequestParam(required = false) String view, HttpSession session, Model model, HttpServletRequest request){
        log.info("==============================개별 프로젝트 controller 진입==============================");
        log.info("project id: {}", projectId);
        log.info("선택한 view: {}", view);
//        log.info("path variable member id: {}", memberId); // 추후

        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
//            URI location = URI.create("/login");
//            return ResponseEntity.created(location).build();
            return "redirect:/login";
        }

        // 2. project
        // 2-1. 프로젝트 정보 - OnTrackProject(프로젝트명, 생성자, 생성일, 유형, 마감일, 상태)
        // 2-2. 해당 프로젝트의 멤버들: List<MemberInfo>
        // 2-3. 할 일 목록 - List<TaskAndAssignee>
        // 2-4. 담당자별 할 일 목록 - List<AssignmentList>
        ProjectResponse project = projectService.getProject(projectId, loginUser.getUserId());
        model.addAttribute("project", project);


        // 3. 마감일 D-Day 계산을 위한 오늘 날짜
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        // 4. 할일 추가 객체(TaskFormRequest)
        model.addAttribute("taskFormRequest", TaskAndAssignee.builder().projectId(projectId).authorMid(project.getMemberId()).build());



        // 5. 할 일 상세 모달의 hide 여부
        Boolean detailOpen = true;
        TaskDetailResponse taskDetail = TaskDetailResponse.builder().build();
        // Task Controller에서 redirect 경우 ↓
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null){
            detailOpen = (Boolean) inputFlashMap.get("hide");

            for(int i = 0; i < project.getTaskList().size(); i++) {
                if(project.getTaskList().get(i).getId().equals(inputFlashMap.get("taskId"))) {

                    // 1) 할 일에 대한 기본 정보 (이미 있기 대문에, 굳이 서비스에 다녀오지 않고)
                    // Project Response 객체 안에서 해당 task에 대한 정보를 추출한다.
                    taskDetail = TaskDetailResponse.entityToDTO(project.getTaskList().get(i), projectId);

                    // 2) comment 가져온다.
                    taskDetail.setTaskComments(taskService.getTaskComment((Long) inputFlashMap.get("taskId")));
//                    log.info("소통하기 글이 없는 상태의 사이즈: {}", taskDetail.getTaskComments().size());
//                    log.info("소통하기 글이 없는 상태의 사이즈: {}", taskDetail.getTaskComments());

                    // 3) history 가져온다.

                }
            }

            taskDetail.setTab((String) inputFlashMap.get("tab"));
//            log.info("task detail이 생성되었는지 확인: {}", taskDetail);
            // task detail이 생성되었는지 확인: TaskAndAssignee(id=8, taskTitle=Tigger can do everything, authorMid=14, authorName=공지철, taskPriority=3, taskStatus=3, taskDueDate=null, taskParentId=null, createdAt=2024-05-24T12:56:29, updatedAt=2024-05-24T12:56:29, updatedBy=14, assigneeMids=[4, 26, 27, 28], assigneeNames=[Adele, 송혜교, 크러쉬, 스칼렛 요한슨], assignees={4=Adele, 26=송혜교, 27=크러쉬, 28=스칼렛 요한슨}, taskFiles=null)

//            log.info("flashMap으로 잡은 flash attribute: {}", inputFlashMap);
            // flashMap으로 잡은 flash attribute: FlashMap [attributes={hide=false}, targetRequestPath=/project/9, targetRequestParams={}]
//            log.info("잡았다! 근데 어떻게 접근하지?: {}", inputFlashMap.get("attributes")); // 잡았다! 근데 어떻게 접근하지?: null
//            log.info("잡았다! 근데 어떻게 접근하지?: {}", inputFlashMap.get("hide")); // 잡았다! 근데 어떻게 접근하지?: false
        }
        // thymeleaf가 taskDetail이 널값일 때 An error happened during template parsing를 던진다... (화면상에서는 문제가 없다.)

        model.addAttribute("taskDetail", taskDetail);
        // 일단 소통하기 글 작성은 fetch로 진행하도록 한다..
//        TaskDetailRequest taskCommentForm = new TaskDetailRequest();
//        taskCommentForm.setProjectId(9L);
//        taskCommentForm.setTaskId(35L);

        // if문(hide != null)안 에 넣으면 계속 에러 남 ㅠㅠ
//        model.addAttribute("taskCommentForm", taskCommentForm);


        log.info("할 일 상세에 대한 hide(detail): {}", detailOpen);
        model.addAttribute("hide", detailOpen);
//        URI projectLocation = URI.create("project/project");

        // null일 경우, table view로 처리한다.
        if(view != null){
            model.addAttribute("view", view);
//            return ResponseEntity.created(projectLocation).body(view);
        }

//        return ResponseEntity.created(projectLocation).build();
        return "project/project"; // url - http://localhost:8080/project/9
    }


}
