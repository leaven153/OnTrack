package me.jhchoi.ontrack.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ErrorResponse;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.service.MemberService;
import me.jhchoi.ontrack.service.ProjectService;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final MemberService memberService;
    private final NavAlarm navAlarm;


    @GetMapping(value = "/myProjects")
    public String myProjects(HttpSession session, Model model){

        log.info("=================myProjects====================");
        // AddProjectRequest의 creator에 로그인한 유저의 id, nickname 담는 코드 추가요망
        model.addAttribute("createProjectRequest", new ProjectRequest());
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            log.info("NO LOGIN INFORMATION");
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
        model.addAttribute("navAlarm", navAlarm.getAlarm(loginUser.getUserId()));
        log.info("MYPAGE CONTROLLER PASS project list: {}", myProjects);

        return "mypage/myProjects";
    }


    @GetMapping("/myTasks")
    public String myTasks(HttpSession session, Model model){
        log.info("=================myTasks====================");
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            log.info("NO LOGIN INFORMATION");
            return "redirect:../login";
        }
        model.addAttribute("loginUser", loginUser);

        List<MyTask> taskList = memberService.getAllMyTasks(loginUser.getUserId());

        model.addAttribute("myTasks", taskList);
        model.addAttribute("navAlarm", navAlarm.getAlarm(loginUser.getUserId()));

        return "mypage/myTasks";
    }

    @GetMapping("/bin")
    public String myBin(HttpSession session, Model model){
        log.info("=================bin====================");
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            log.info("NO LOGIN INFORMATION");
            return "redirect:../login";
        }
        // 영구 삭제의 권한은 누구에게? >> 할 일 작성자 only? (생성자creator, 관리자admin??)
        model.addAttribute("loginUser", loginUser);
        log.info("PASSED BY CONTROLLER - userId: {}", loginUser.getUserId());
        List<BinResponse> binTaskList = taskService.getMyBin(loginUser.getUserId());
        log.info("CONTROLLER GOT THIS - binTaskList: {}", binTaskList);
        model.addAttribute("binTaskList", binTaskList);
        model.addAttribute("navAlarm", navAlarm.getAlarm(loginUser.getUserId()));
        return "mypage/bin";
    }


    @GetMapping("/removedTask")
    public ResponseEntity<?> binTaskRow(@RequestParam Long taskId){
        log.info("BIN TASK ROW ELEMENT CREATE - task id: {}", taskId);
        BinResponse binTaskRow = taskService.binTaskRow(taskId);
        if(binTaskRow == null) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
        }
        return ResponseEntity.ok().body(binTaskRow);
    }
}
