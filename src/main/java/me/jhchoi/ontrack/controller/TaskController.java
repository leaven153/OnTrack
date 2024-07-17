package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.spring6.view.ThymeleafView;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;


    @PostMapping("/addTask")
    public String addTaskSubmit(@ModelAttribute TaskFormRequest taskFormRequest, HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        MemberInfo member = (MemberInfo) session.getAttribute("loginMember");
        if (loginUser == null) {
            return "redirect:/login/login";
        }
        log.info("=============from 할일추가 form==================");
        log.info("프로젝트아이디 = {}", taskFormRequest.getProjectId());
        log.info("작성자아이디 = {}", taskFormRequest.getTaskAuthorMid());
        log.info("작성자 이름 = {}", taskFormRequest.getAuthorName());
        log.info("할일 이름 = {}", taskFormRequest.getTaskTitle());
        log.info("전체 = {}", taskFormRequest);
//        log.info("파일 = {}", taskFormRequest.getTaskFile().get(0).getOriginalFilename());

        // admin이나 creator가 아닌 member가 생성한 할 일의 중요도는 null값임. 고로, 일반(2)으로 설정하여 service로 넘긴다.
        if (taskFormRequest.getTaskPriority() == null) taskFormRequest.setTaskPriority(2);
        taskService.addTask(taskFormRequest);

        log.info("컨트롤러에서 넘어가는 시점: {}", LocalDateTime.now()); // 컨트롤러에서 넘어가는 시점: 2024-06-04T17:50:39.535349900
        // fetch 에서 response 없애고 2024-06-05T21:45:00.923132600
        String url = """
                redirect:/project/%s
                """.formatted(taskFormRequest.getProjectId());
        return url;
    }

    // 할 일 상세 fragment만 rendering
    @Bean(name="taskDetail")
    @Scope("prototype")
    public ThymeleafView taskDetailViewBean(HttpSession session){
        ThymeleafView projectView = new ThymeleafView("taskDetail");
        projectView.setMarkupSelector("#container-task-detail");
        log.info("이게 언제 불려지나? === fragment bean"); // WebConfig에 있어도, 현 컨트롤러 안에 있어도 2번 출력된다.
        log.info("fragment안에서 session: {}", session.getServletContext());
        // fragment안에서 session: session ☞ Current HttpSession
        // session.getServletContext() ☞ org.apache.catalina.core.ApplicationContextFacade@c0ce5b6
        return projectView;
    }


    @GetMapping("/detail/{taskId}")
    public String getTaskDetail(@PathVariable Long taskId, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request){
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if(loginUser == null) {
//            URI location = URI.create("/login");
//            return ResponseEntity.created(location).build();
            return "redirect:/login";
        }
        log.info("======== getTaskDetail 컨트롤러 진입 ========");

        TaskDetailRequest tr = new TaskDetailRequest();
        redirectAttributes.addFlashAttribute("hide", false);
        tr.setProjectId(9L);

        return "redirect:/project/%s".formatted(tr.getProjectId());

//        return ResponseEntity.ok().body(th);
//        return TaskList.builder().authorName("testAuthor").build();
//        return "fragments/taskDetail :: editForm";
//        String encodedName = URLEncoder.encode(loginMember.getNickName(), StandardCharsets.UTF_8);
//        return """
//                redirect:/project/%s/%s/%s/%s""".formatted(loginMember.getProjectId(), loginMember.getMemberId(), encodedName, loginMember.getPosition());
    } // getTask ends


    @PostMapping("/{taskId}/comment")
    public String taskComment(@PathVariable Long taskId, @ModelAttribute TaskDetailRequest taskDetailRequest){
        log.info("**************comment controller enter :) ***************");
        log.info("무엇이 작성되어 왔나: {}", taskDetailRequest);
        // 무엇이 작성되어 왔나: TaskDetailRequest(projectId=9, taskId=35, authorMid=14, authorName=공지철, comment=모두확인요청은 또 어떻게 풀거니..?, commentType=null, fileName=null)
        return "redirect:/task/detail/%s".formatted(taskId);
    }
    /**
     * 할 일의 내용 수정: 할 일 명(title), 진행상태(status), 마감일(dueDate), 중요도(priority)
     * 리퀘스트 파라미터로 어떤 항목(item)을 바꾸는지 받고
     * 리퀘스트 바디로 해당 항목의 새 내용을 받는다.
     * ontrack_task와 task_history 테이블에 해당 내용을 반영한 후, return 값으로 해당 내용을 보낸다.
     * 담당자 배정/해제에 관한 변경사항은 별도의 컨트롤러에서 처리한다.(editAssignee)
     * */
    @PostMapping("/editTask")
    public ResponseEntity<?> editTask(HttpSession session, @RequestParam String item, @RequestParam(required = false) String statusNum, @RequestBody TaskHistory th){
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            URI location = URI.create("/login");
            return ResponseEntity.created(location).build();
        }
        log.info("=================================editTask Controller 접근=================================");
        log.info("어떤 항목을 바꿀 건가요? {}", item);
        log.info("task history: {}", th);

        // 변경시간 입력
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();
        th.setUpdatedAt(nowWithNano.minusNanos(nanosec));

        if (th.getModItem().equals("title")){
            log.info("할 일 제목 수정");

        } else if (th.getModItem().equals("dueDate")) {
            log.info("할 일 마감일 수정");
        } else if (th.getModItem().equals("status")) {
            log.info("할 일 진행상태 수정");
            log.info("변경된 진행상태: {}", statusNum);
            TaskEditRequest ter = TaskEditRequest.builder()
                    .taskId(th.getTaskId())
                    .status(Integer.valueOf(statusNum))
                    .updatedAt(th.getUpdatedAt())
                    .updatedBy(th.getUpdatedBy())
                    .build();

            return taskService.editTaskStatus(th, ter);
//            log.info("진행상태 한글로: {}", TaskList.switchStatusToKor(Integer.parseInt(th.getModContent())));


        }

        return new ResponseEntity<>("결과스트링", HttpStatus.OK);
    }

    @PostMapping("/editAssignee")
    @ResponseBody
    public ResponseEntity<?> editAssignee(HttpSession session, @RequestParam Long mid, @RequestBody TaskHistory th) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            URI location = URI.create("/login");
            return ResponseEntity.created(location).build();
        }
        log.info("============= edit Assignee Controller 진입 =================");
        log.info("member id?: {}", mid);
        log.info("task history 객체: {}", th);

        // 배정/해제한 시간
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();
        th.setUpdatedAt(nowWithNano.minusNanos(nanosec));

        if (th.getModType().equals("register")){
            log.info("담당자 추가");
            // TaskAssignment 객체생성
            TaskAssignment ta = TaskAssignment.builder()
                    .projectId(th.getProjectId())
                    .taskId(th.getTaskId())
                    .memberId(mid)
                    .nickname(th.getModContent())
                    .role(th.getModItem())
                    .assignedAt(nowWithNano.minusNanos(nanosec))
                    .build();

            return taskService.addAssignee(ta, th);

        } else if (th.getModType().equals("delete")){
            log.info("담당자 삭제");
            TaskAssignment ta = TaskAssignment.builder()
                    .projectId(th.getProjectId())
                    .taskId(th.getTaskId())
                    .memberId(mid)
                    .nickname(th.getModContent())
                    .role(th.getModItem())
                    .assignedAt(nowWithNano.minusNanos(nanosec))
                    .build();
            int result = taskService.unassign(ta, th);
            if(result != 1) {
                // 해당 task id나 member id가 없을 경우라 가정...
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("담당자 삭제가 이뤄지지 않았습니다.");
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/search")
    public ResponseEntity<?> searchMemberToAssign(@RequestParam String object, @RequestParam(required = false) String purpose, @RequestBody GetMemberNameRequest searchCond){
        log.info("무엇을 찾는가: {}", object); // 무엇을 찾는가: member
        log.info("무엇을 위해 찾는가: {}", purpose); // 무엇을 위해 찾는가: toassign
        log.info("검색할 이름: {}", searchCond); //검색할 이름: GetMemberNameRequest(projectId=9, taskId=14, userId=null, memberId=null, nickname=adele)

        // 검색한 사람이 이미 담당자인 경우

        // 검색한 사람이 없는 경우

        return new ResponseEntity<>(searchCond.getNickname(), HttpStatus.OK);
    }

}// class TaskController ends
