package me.jhchoi.ontrack.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.*;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.repository.ProjectRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import me.jhchoi.ontrack.service.MemberService;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    /**
     * created : 2024-05-
     * param   : @ModelAttribute TaskAndAssignee
     * return  : ResponseEntity
     * explain : 할 일 추가
     * */
    @PostMapping
    public ResponseEntity<?> addTaskSubmit(@ModelAttribute TaskAndAssignee taskFormRequest, HttpSession session) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            URI location = URI.create("/login");
            return ResponseEntity.created(location).build();
//            return "redirect:/login/login";
        }
        log.info("=============from 할일추가 form==================");

        log.info("전체 = {}", taskFormRequest);
        // 전체 = TaskAndAssignee(id=null, taskTitle=파일 테스트, authorMid=6, authorName=크러쉬,
        // taskPriority=3, taskStatus=null, taskDueDate=2024-08-14, taskParentId=null,
        // createdAt=null, updatedAt=null, updatedBy=null, projectId=11,
        // assigneeNum=null, assigneeMid=null, assigneeName=null,
        // assigneeMids=[6, 17], assigneeNames=[크러쉬, 토르], assignees=null,
        // taskFiles=[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@1a2f2550])

        // admin이나 creator가 아닌 member가 생성한 할 일의 중요도는 null값임. 고로, 일반(3)으로 설정하여 service로 넘긴다.
//        if (taskFormRequest.getTaskPriority() == null) taskFormRequest.setTaskPriority(3);
        taskService.addTask(taskFormRequest);

//        log.info("컨트롤러에서 넘어가는 시점: {}", LocalDateTime.now()); // 컨트롤러에서 넘어가는 시점: 2024-06-04T17:50:39.535349900
        // fetch 에서 response 없애고 2024-06-05T21:45:00.923132600
        return ResponseEntity.ok(HttpStatus.OK);
//        return """
//                redirect:/project/%s
//                """.formatted(taskFormRequest.getProjectId());
    }


    /**
     * created : 2024-07-
     * param   : @PathVariable: Long taskId, @RequestParam: String tab
     * return  : String(redirect - getProject)
     * explain : 할 일 상세 모달(탭) 열기
     * */
    @GetMapping("/detail/{taskId}")
    public String getTaskDetail(@PathVariable Long taskId, @RequestParam String tab, HttpSession session, RedirectAttributes redirectAttributes){
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if(loginUser == null) {
//            URI location = URI.create("/login");
//            return ResponseEntity.created(location).build();
            return "redirect:/login";
        }
        log.info("======== getTaskDetail 컨트롤러 진입 ========");

        Optional<OnTrackTask> task = taskRepository.findByTaskId(taskId);
        log.info("task가 어떻게 받아와지는가: {}", task);
        log.info("어떤 탭을 보여줘야 하는가: {}", tab);
        // task가 어떻게 받아와지는가: Optional[OnTrackTask(id=null, projectId=9, taskTitle=Tigger can do everything, authorMid=14, authorName=공지철, taskPriority=3, taskStatus=3, taskDueDate=null, taskParentId=null, createdAt=2024-05-24T12:56:29, updatedAt=2024-05-24T12:56:29, updatedBy=14)]

        Boolean detailHide = false;
        log.info("삭제된 할 일의 상세모달 오픈 요청을 했을 때(task의 getDeletedBy): {}", task.get().getDeletedBy());
        if(task.isPresent() && task.get().getDeletedBy() != null){
            detailHide = true;
            redirectAttributes.addFlashAttribute("taskRemoved", true);
        }
        redirectAttributes.addFlashAttribute("hide", detailHide);
        redirectAttributes.addFlashAttribute("taskId", taskId);
        redirectAttributes.addFlashAttribute("tab", tab);

        return "redirect:/project/%s".formatted(task.get().getProjectId());

//        return ResponseEntity.ok().body(th);
//        return TaskAndAssignee.builder().authorName("testAuthor").build();
//        return "fragments/taskDetail :: editForm";
//        String encodedName = URLEncoder.encode(loginMember.getNickName(), StandardCharsets.UTF_8);
    } // getTask ends



    /**
     * created : 2024-07-
     * param   : @PathVariable: Long taskId, @RequestParam: String type(등록/수정), @RequestBody: TaskDetailRequest- 내용
     * return  : ResponseEntity
     * explain : 할 일 상세: 소통하기 글 등록·수정
     * */
    @PostMapping(value = "/comment")
    public ResponseEntity<?> taskComment(@RequestParam String type, @RequestBody TaskDetailRequest taskDetailRequest) throws ParseException {
        log.info("**************comment controller enter :) ***************");
//        log.info("task id: {}", taskId); // @PathVariable Long taskId,
        log.info("등록이요 수정이요: {}", type);
        log.info("무엇이 작성되어 왔나: {}", taskDetailRequest);
        // 무엇이 작성되어 왔나: TaskDetailRequest(projectId=9, taskId=8, authorMid=14, authorName=공지철,
        // comment=Tigger can do everything, commentType=normal,
        // createdAt=24.07.19 14:16, updatedAt=24.07.19 14:16, fileName=null)

        ResponseEntity<?> response = null;
        // 1. 새 글 등록인지, 수정인지 확인한다.
        if(Objects.equals(type, "add")) {
            TaskComment taskComment = taskDetailRequest.toTaskComment(taskDetailRequest);
            response = taskService.addTaskComment(taskComment);

        } else if(Objects.equals(type, "edit")) {
            log.info("소통하기 글 수정: {}", taskDetailRequest);
            TaskComment editComment = taskDetailRequest.toTaskCommentforEdit(taskDetailRequest);
            response = taskService.editTaskComment(editComment);
        } else if(Objects.equals(type, "blocked")) {
            log.info("관리자에 의한 소통하기 글 차단(수정): {}", taskDetailRequest);
            LocalDateTime nowWithNano = LocalDateTime.now();
            int nanosec = nowWithNano.getNano();
            taskDetailRequest.setBlockedAt(nowWithNano.minusNanos(nanosec));
            response = taskService.blockTaskComment(taskDetailRequest);
        }

        return response;
    }

    /**
     * created : 2024-08-06
     * param   : @PathVariable: Long commentId
     * return  : ResponseEntity
     * explain : 할 일 상세: 소통하기 글 삭제
     * */
    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@RequestParam Long cId){
        log.info("작성자에 의한 글삭제: {}", cId);
        return taskService.delComment(cId);
    }

    /**
     * created : 2024-08-02
     * param   :
     * return  : ResponseEntity
     * explain : 파일 업로드
     * */
    @PostMapping("/file") // /upload
    public ResponseEntity<?> uploadFile(@RequestParam Long pId, @RequestParam Long tId, @RequestParam Long mId, @RequestBody List<MultipartFile> files){ //
        log.info("******** 파일을 업로드하려고 해 ********");
//        log.info("어떻게 받아와지나: {}", tdr);
        log.info("어떻게 받아와지나: {}", pId);
        log.info("어떻게 받아와지나: {}", tId);
        log.info("어떻게 받아와지나: {}", mId);
        log.info("어떻게 받아와지나: {}", files);

        TaskDetailRequest tdr = TaskDetailRequest.builder()
                .projectId(pId)
                .taskId(tId)
                .authorMid(mId)
                .taskFiles(files)
                .build();

        return taskService.attachFile(tdr);
        //ResponseEntity.ok().body(testFileList);
        // 어떻게 받아와지나: [org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@682c75cc, org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@6c29a498, org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@3e52955f]
//        return ResponseEntity.ok("file upload ing");
    }

    /**
     * created : 2024-08-01
     * param   : Long fileId
     * return  : ResponseEntity
     * explain : 파일 다운로드
     * */
    @GetMapping("/file/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId) throws MalformedURLException {
        log.info("************** 파일을 다운받으러 왔다 **************");
        TaskFile file = taskRepository.findFileById(fileId);
        UrlResource resource = new UrlResource("file:"+file.getFilePath()+"/"+file.getFileNewName());
        String encodedOriginFileName = UriUtils.encode(file.getFileOrigName()+"."+file.getFileType(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=" + encodedOriginFileName;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    /**
     * created : 2024-08-04
     * param   : Long fileId
     * return  : ResponseEntity
     * explain : 파일 삭제
     * */
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam Long fId, @RequestParam(required = false) Long executorMid){

        ResponseEntity<?> response;
        if(executorMid != null) {
            log.info("관리자에 의한 파일 삭제: {}", fId);
            log.info("관리자에 의한 파일 삭제: {}", executorMid);
            LocalDateTime nowWithNano = LocalDateTime.now();
            int nanosec = nowWithNano.getNano();

            TaskFile deleteItem = TaskFile.builder()
                    .id(fId)
                    .deletedBy(executorMid)
                    .deletedAt(nowWithNano.minusNanos(nanosec))
                    .build();
            response = taskService.deleteFileByAdmin(deleteItem);
        } else {
            log.info("올린이에 의한 파일 삭제: {}", fId);
            response = taskService.delFile(fId);
        }
        return response; // ResponseEntity.ok("확인중");
    }


    /**
     * created : 2024-0567-
     * param   : @RequestParam: item- 어떤 항목을 바꾸는가, @RequestBody: taskHistory- 바꾸는 내용
     * return  : ResponseEntity
     * explain : 할 일 수정: 할 일 명(title), 진행상태(status), 마감일(dueDate), 중요도(priority))
     * */
    @PostMapping("/editTask")
    public ResponseEntity<?> editTask(HttpSession session, @RequestParam String item, @RequestParam(required = false) String statusNum, @RequestBody TaskHistory th) {
        LoginUser loginUser = (LoginUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            URI location = URI.create("/login");
            return ResponseEntity.created(location).build();
        }
        log.info("=== editTask Controller 접근 ===");
        log.info("어떤 항목을 바꿀 건가요? {}", item);
        log.info("task history: {}", th);

        // 변경시간 입력
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();
        th.setUpdatedAt(nowWithNano.minusNanos(nanosec));

        // 각 수정 사항 반영 결과값 담을 ResponseEntity
        ResponseEntity<?> response = null;

        switch (th.getModItem()) {
            case "할 일 명" -> {
                log.info("할 일 제목 수정");
                TaskEditRequest editTaskTitle = TaskEditRequest.builder()
                        .taskId(th.getTaskId())
                        .title(th.getModContent())
                        .updatedAt(th.getUpdatedAt())
                        .updatedBy(th.getUpdatedBy())
                        .build();
                response = taskService.editTaskTitle(th, editTaskTitle);
            }
            case "마감일" -> {
                log.info("할 일 마감일 수정");

                // TaskHistory(id=null, taskId=14, projectId=9, modItem=dueDate, modType=update, modContent=2024-07-31, updatedAt=null, updatedBy=14)
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                TaskEditRequest editTaskDueDate = TaskEditRequest.builder()
                        .taskId(th.getTaskId())
                        .updatedAt(th.getUpdatedAt())
                        .updatedBy(th.getUpdatedBy())
                        .build();
                if (Objects.equals(th.getModContent(), "")) {
                    th.setModContent("마감일 삭제");
                    editTaskDueDate.setDueDate(null);
                } else {
                    editTaskDueDate.setDueDate(LocalDate.parse(th.getModContent(), dateFormatter));
                }
                response = taskService.editTaskDueDate(th, editTaskDueDate);
            }
            case "진행상태" -> {
                log.info("할 일 진행상태 수정");
                log.info("변경된 진행상태: {}", statusNum);
                TaskEditRequest ter = TaskEditRequest.builder()
                        .taskId(th.getTaskId())
                        .status(Integer.valueOf(statusNum))
                        .updatedAt(th.getUpdatedAt())
                        .updatedBy(th.getUpdatedBy())
                        .build();
                response = taskService.editTaskStatus(th, ter);
            }
        }

        return response;
    }

    /**
     * created : 2024-0567-
     * param   : @RequestParam: mid- 배정/해제되는 멤버id, @RequestBody: taskHistory
     * return  : ResponseEntity
     * explain : 담당자 배정/해제
     * */
    @PostMapping("/editAssignee")
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

        ResponseEntity<?> response = null;

        if (Objects.equals(th.getModType(), "배정")){
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

            response = taskService.addAssignee(ta, th);

        } else if (Objects.equals(th.getModType(), "해제")){
            log.info("담당자 삭제");
            TaskAssignment ta = TaskAssignment.builder()
                    .projectId(th.getProjectId())
                    .taskId(th.getTaskId())
                    .memberId(mid)
                    .nickname(th.getModContent())
                    .role(th.getModItem())
                    .assignedAt(nowWithNano.minusNanos(nanosec))
                    .build();
            response = taskService.unassign(ta, th);
        }

        return response;
    }

    /**
     * created : 2024-07-
     * param   : @RequestParam: String object- 찾는 항목(member/task/...), @RequestBody: SearchCond- 찾을 내용
     * return  : ResponseEntity
     * explain : 검색: ① 미배정멤버
     * */
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestParam String object, @RequestBody SearchCond searchCond){
        log.info("무엇을 찾는가: {}", object); // 무엇을 찾는가: member
        log.info("검색할 이름: {}", searchCond); //검색할 이름:


        // member service에서 해당 이름을 가진 멤버가 해당 프로젝트에 존재하는지 먼저 검색
        ResponseEntity<List<ProjectMember>> existsMember = memberService.findByName(searchCond);
        log.info("task controller - exists member: {}", existsMember);

        if(existsMember.getStatusCode().is4xxClientError()){
            return ResponseEntity.ok().body(existsMember);
        }

        List<TaskAndAssignee> result = new ArrayList<>();

        // 해당 이름을 가진 멤버가 존재한다면
        // 해당 task에 배정됐는지 확인한다.
        List<ProjectMember> member = existsMember.getBody(); // (List<ProjectMember>)
        log.info("해당 이름을 가진 멤버 존재: {}",member);

        for (ProjectMember projectMember : member) {
            TaskAssignment request = TaskAssignment.builder()
                    .taskId(searchCond.getTaskId())
                    .memberId(projectMember.getId()).build();
            if (taskRepository.chkAssigned(request) == null) {
                log.info("해당 task에 이미 배정됐는가(task ID가 return 됐다면 이미 배정된 멤버임): {}", taskRepository.chkAssigned((request)));
                result.add(TaskAndAssignee.builder()
                        .assigneeMid(String.valueOf(projectMember.getId()))
                        .assigneeName(projectMember.getNickname()).build());
            }
        }

        log.info("전달 직전 result: {}", result);
        return ResponseEntity.ok().body(result); //new ResponseEntity<>(listRs, HttpStatus.OK); // ResponseEntity.ok().body("none");
    }

    /**
     * created : 2024-08-06
     * param   : @ModelAttribute TaskAndAssignee
     * return  : ResponseEntity
     * explain : 프로젝트에서 할 일 삭제
     * */
    @DeleteMapping
    public ResponseEntity<Void> moveToBin(@RequestBody BinRequest taskBinRequest) { // @RequestBody Map<Long, Long> testId
        log.info("할 일을 여러 개 지우려면 어떻게 받아오면 될까: {}", taskBinRequest);
        // 할 일을 여러 개 지우려면 어떻게 받아오면 될까:
        // BinRequest(
        // projectAndTaskId=[{}, {}, {projectId=9, taskId=10}],
        // taskIds=null, deletedBy=14, deletedAt=null, testId=null, projectId=null,
        // taskId=null, restoredBy=null, restoredAt=null)

        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();
        taskBinRequest.setDeletedAt(nowWithNano.minusNanos(nanosec));
        taskService.moveToBin(taskBinRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteTaskTest(@RequestBody Map<Long, Long> testId) { // @RequestBody BinRequest taskBinRequest
        //그냥 map으로만 받아오기
        log.info("testId: {}", testId);
        log.info("size: {}", testId.size());
        log.info("keySet: {}", testId.keySet());
        // @RequestBody Map<Long, Long> testId, body: JSON.stringify({9: 10})
        // ☞ 혹시 mapping type문제일까?: {9=10}
        // 혹시 mapping type문제일까?: {}
        return ResponseEntity.ok("테스트중");
    }

    /**
     * created : 2024-08-
     * param   : @RequestBody BinRequest
     * return  : ResponseEntity
     * explain : 할 일 여러 개 복원(휴지통 → 프로젝트)
     * */
    @PostMapping("/restore")
    public ResponseEntity<?> restoreMultiTasks(@RequestBody BinRequest taskBinRequest){
        log.info("할 일 여러 개를 휴지통에서 프로젝트로 복원할 때: {}", taskBinRequest);
        log.info("할 일 여러 개를 휴지통에서 프로젝트로 복원할 때: {}", taskBinRequest.getProjectAndTaskId().size());
        log.info("할 일 여러 개를 휴지통에서 프로젝트로 복원할 때: {}", taskBinRequest.getProjectAndTaskId().get(0));

        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();
        taskBinRequest.setDeletedAt(nowWithNano.minusNanos(nanosec));
        return ResponseEntity.ok("할 일 삭제중");
    }

    /**
     * created : 2024-08-
     * param   : @RequestParam
     * return  : ResponseEntity
     * explain : 할 일 1개 복원(휴지통 → 프로젝트)
     * */
    @GetMapping("/restore")
    public String restoreTask(@RequestParam Long pId, @RequestParam Long tId, @RequestParam(required = false) Long mId, @RequestParam(required = false) Long uId){
        log.info("1개만 복원할 때 pid, {}", pId); // 1개만 복원할 때 pid, 9
        log.info("1개만 복원할 때 tid, {}", tId); // 1개만 복원할 때 tid, 20
        log.info("1개만 복원할 때 mid, {}", mId); // 1개만 복원할 때 mid, 14
        log.info("동적으로 생성된 task row에 복원하기 눌렀을 때, {}", uId);

        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanoSec = nowWithNano.getNano();

        // 동적으로 생성된 bin task row에서 복원요청이 왔을 때: uId
        // user id와 project id로 member id 찾는다.
        if (mId == null){
            List<MemberInfo> memberInfo = projectRepository.getMemberInfo(MemberInfo.builder().projectId(pId).userId(uId).build());
            mId = memberInfo.get(0).getMemberId();
        }


        // history 남긴다.
        taskRepository.log(TaskHistory.builder()
                .projectId(pId)
                .taskId(tId)
                .modItem("할 일")
                .modType("복원")
                .modContent("휴지통에서 프로젝트로")
                .updatedBy(mId)
                .updatedAt(nowWithNano.minusNanos(nanoSec))
                .build());

        // ontrack_task update
        taskRepository.taskSwitchBin(OnTrackTask.builder().id(tId).deletedAt(null).deletedBy(null).build());
        return "redirect:/mypage/bin";// ResponseEntity.ok("할 일 한 개 복원");
    }

}// class TaskController ends
