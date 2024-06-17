package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.MemberList;
import me.jhchoi.ontrack.dto.TaskFormRequest;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.dto.TaskList;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.spring6.view.ThymeleafView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;


    @PostMapping("/addTask")
    public RedirectView addTaskSubmit(@ModelAttribute TaskFormRequest taskFormRequest, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        MemberList member = (MemberList) session.getAttribute("loginMember");
        if (loginUser == null) {
            return new RedirectView("login/login");
//            return "redirect:/login/login";
        }
        log.info("=============from 할일추가 form==================");
        log.info("프로젝트아이디 = {}", taskFormRequest.getProjectId());
        log.info("작성자아이디 = {}", taskFormRequest.getTaskAuthorMid());
        log.info("작성자 이름 = {}", taskFormRequest.getAuthorName());
        log.info("할일 이름 = {}", taskFormRequest.getTaskTitle());
        log.info("전체 = {}", taskFormRequest);
//        log.info("파일 = {}", taskFormRequest.getTaskFile().get(0).getOriginalFilename());

        // admin이나 creator가 아닌 member가 생성한 할 일의 중요도는 null값임. 고로, 일반으로 설정하여 service로 넘긴다.
        if (taskFormRequest.getTaskPriority() == null) taskFormRequest.setTaskPriority(2);
        Long newTaskId = taskService.addTask(taskFormRequest);

        String encodedName = URLEncoder.encode(taskFormRequest.getAuthorName(), StandardCharsets.UTF_8);
        log.info("컨트롤러에서 넘어가는 시점: {}", LocalDateTime.now()); // 컨트롤러에서 넘어가는 시점: 2024-06-04T17:50:39.535349900
        // fetch 에서 response 없애고 2024-06-05T21:45:00.923132600

        redirectAttributes.addAttribute("projectId", taskFormRequest.getProjectId());
        redirectAttributes.addAttribute("memberId", taskFormRequest.getTaskAuthorMid());
        redirectAttributes.addAttribute("nickname", taskFormRequest.getAuthorName());
        redirectAttributes.addAttribute("position", member.getPosition());

//        return "redirect:/task/test";
        return new RedirectView("/project/{projectId}/{memberId}/{nickname}/{position}");

//        return """
//                redirect:/project/%s/%s/%s
//                """.formatted(taskFormRequest.getProjectId(), taskFormRequest.getTaskAuthorMid(), encodedName);
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

    @PostMapping("getTask/{taskId}/{memberId}")
    public String getTask(@PathVariable("taskId") Long taskId, @PathVariable("memberId") Long memberId,
                            @RequestBody MemberList loginMember, HttpSession session, Model model){
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if(loginUser == null) {
            return "login/login";
        }
        log.info("======== getTask 컨트롤러 진입 ========");
        log.info("taskId: {}", taskId);
        log.info("memberId: {}", memberId);
        log.info("RequestBody: {}", loginMember);
        String test = "testString";
        model.addAttribute("test", test);
//        return TaskList.builder().authorName("testAuthor").build();
//        return "fragments/taskDetail :: editForm";
        String encodedName = URLEncoder.encode(loginMember.getNickName(), StandardCharsets.UTF_8);
        return """
                redirect:/project/%s/%s/%s/%s""".formatted(loginMember.getProjectId(), loginMember.getMemberId(), encodedName, loginMember.getPosition());
    } // getTask ends


    /**
     * 할 일의 내용 수정: 할 일 명(title), 진행상태(status), 마감일(dueDate), 중요도(priority)
     * 리퀘스트 파라미터로 어떤 항목(item)을 바꾸는지 받고
     * 리퀘스트 바디로 해당 항목의 새 내용을 받는다.
     * ontrack_task와 task_history 테이블에 해당 내용을 반영한 후, return 값으로 해당 내용을 보낸다.
     * 담당자 배정/해제에 관한 변경사항은 별도의 컨트롤러에서 처리한다.(editAssignee)
     * */
    @PostMapping("/editTask")
    @ResponseBody
    public String editTask(HttpSession session, @RequestParam(required = false)String item, @RequestBody String edit){
        log.info("=================================editTask Controller 접근=================================");
        log.info("어떤 항목을 바꿀 건가요? {}", item);
        log.info("제목: {}", edit);
        return edit;
    }

    @PostMapping("/editAssignee")
    @ResponseBody
    public String editAssignee(HttpSession session, @RequestParam(required = false) Long taskId, @RequestParam(required = false) Long execMid, @RequestParam(required = false)String addOrDel, @RequestParam(required = false) Long mId, @RequestBody String mName) {
        log.info("============= edit Assignee Controller 진입 =================");
        log.info("which task?: {}", taskId);
        log.info("누가 변경을 진행했나요: {}", execMid);
        log.info("삭제입니까 배정입니까: {}", addOrDel);
        log.info("어떤 멤버를 배정 혹은 삭제합니까: {}", mId);
        log.info("어떤 멤버를 배정 혹은 삭제합니까: {}", mName);
        
        // task_assignment에 반영
        
        // task_history에 반영

        return null;
    }

}// class TaskController ends
