package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.*;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final FileStore fileStore;
    /**
     * created  : 2024-05-14
     * updated  : 2024-05-23, 24
     * param    :
     * return   :
     * explain  : 새 할 일 등록 (할 일 정보, 담당자(nullable), 파일(nullable))
     * */
    @Transactional
    public Long addTask(TaskAndAssignee taskFormRequest) {

        // 1. 할 일 객체 생성
        OnTrackTask task = taskFormRequest.dtoToEntityTask();

        // 1-1. 할 일 등록
        taskRepository.newTask(task);

        // 2. history 등록 - ①할 일 명,
        taskRepository.log(TaskHistory.logNewTask(task));
        
        // 3. 담당자 유무 확인
        if (taskFormRequest.getAssigneeMid() != null && !taskFormRequest.getAssigneeMid().isEmpty()) {

            taskFormRequest.setAssigneeNames(new ArrayList<>());
            // 3-1. 담당자 nickname 가져오기
            for(int i= 0; i < taskFormRequest.getAssigneeMids().size(); i++){
                log.info("task service에서 멤버 id: {}", taskFormRequest.getAssigneeMids().get(i));
                List<MemberInfo> mList = projectRepository.getMemberInfo(MemberInfo.builder().projectId(taskFormRequest.getProjectId()).memberId(taskFormRequest.getAssigneeMids().get(i)).build());
                taskFormRequest.getAssigneeNames().add(mList.get(0).getNickname());
            }

            // 3-2. 담당자 객체(TaskAssignment) 생성 및 DB 저장
            List<TaskAssignment> assignees = taskFormRequest.dtoToEntityTaskAssignment(task.getId(), task.getCreatedAt());
            taskRepository.assign(assignees);
            
            // 3-3. history 등록 - ② 담당자 인원만큼 history 객체 생성 및 DB 저장
            IntStream.range(0, assignees.size()).forEach(i -> taskRepository.log(TaskHistory.logAssignment(assignees.get(i), task.getAuthorMid())));
        }

        // 4. 파일첨부 여부 check 후 해당 프로젝트/할일 폴더에 저장 및 TaskFile 객체 생성
        // task 생성 후에 task Id를 가지고 file을 저장할 수 있다.
        // Cannot invoke "java.util.List.isEmpty()" because the return value of "me.jhchoi.ontrack.dto.TaskAndAssignee.getTaskFiles()" is null
        if (taskFormRequest.getTaskFiles() != null && !taskFormRequest.getTaskFiles().isEmpty()) {
            try {
                List<TaskFile> fList = fileStore.storeFile(taskFormRequest.getTaskFiles(), task.getProjectId(), task.getId(), task.getAuthorMid(), task.getCreatedAt());
                log.info("task service에서 file list: {}", fList);
                if(fList != null && !fList.isEmpty()) taskRepository.attachFile(fList);
            } catch (IOException e) {
                log.info("파일 저장 에러: {}", e.getMessage());
                // 파일 저장 에러: java.io.FileNotFoundException:
                /**
                 * C:\Users/user\AppData\Local\Temp\tomcat.8080.2139694289704046896\work\Tomcat\localhost\ROOT\9\14\9bba9073-0957-4dd9-acb4-0e3103105f27.txt (지정된 경로를 찾을 수 없습니다)
                 * */
            }
        }

        // 5. 마감일 유무 확인 후 history insert
        if(taskFormRequest.getTaskDueDate() != null) {
            taskRepository.log(TaskHistory.logDueDate(task));
        }

        return task.getId();
    }

    /*
     * created : 2024-07-31
     * param   : TaskEditRequest
     * return  : ResponseEntity
     * explain : 할 일 수정: 할 일 명
     * */
    @Transactional
    public ResponseEntity<?> editTaskTitle(TaskHistory th, TaskEditRequest ter){
        taskRepository.log(th);
        Integer result = taskRepository.editTaskTitle(ter);
        if (result != 1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(ter.getTitle());
    }

    /*
     * created : 2024-07-29
     * param   :
     * return  :
     * explain : 할 일 수정: 진행상태
     * */
    @Transactional
    public ResponseEntity<?> editTaskStatus(TaskHistory th, TaskEditRequest ter){

        // 진행상태는 담당자가 없을 경우, 시작 안함 상태가 될 수 없다.
        Integer assignedNum = taskRepository.cntAssigneeByTaskId(ter.getTaskId());
        if(assignedNum != null) {
            taskRepository.log(th);
            Integer result = taskRepository.editTaskStatus(ter);
            if(result == 1) {
                return ResponseEntity.ok().body(ter.getStatus());
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("진행상태 변경이 완료되지 않았습니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("담당자가 없는 할 일은 진행상태를 바꿀 수 없습니다."));
        }
    }

    /*
     * created : 2024-07-30
     * param   : TaskHistory, TaskEditRequest
     * return  : ResponseEntity
     * explain : 할 일 수정: 마감일
     * */
    @Transactional
    public ResponseEntity<?> editTaskDueDate(TaskHistory th, TaskEditRequest ter){
        taskRepository.log(th);
        Integer result = taskRepository.editTaskDueDate(ter);
        if(result == 1) {
            return ResponseEntity.ok().body("");
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("마감일 변경이 완료되지 않았습니다."));
        }
    }

    /*
     * created : 2024-06-18
     * param   : TaskAssignment, TaskHistory
     * return  : void
     * explain : 할 일 수정: 담당자 추가
     * */
    @Transactional
    public ResponseEntity<?> addAssignee(TaskAssignment ta, TaskHistory th){

        // 예외는 어떻게 처리해야 할까?
        // 이미 삭제된 task 혹은 project에 담당자를 배정하려고 하는 경우?
        // assign에는 그닥 오류가 없을 수 있다. 없는 task, project라 해도
        // 이를 test하고 저장하지 않으니까...

        // 해당 일에 이미 배정(참여)된 담당자인지 확인 필요
        Long assigned = taskRepository.chkAssigned(ta);
        if (ta.getTaskId().equals(assigned)) {
            return ResponseEntity.badRequest().body("이미 배정된 담당자입니다.");
        }

        Integer LIMIT_ASSIGN = 6;
        // 담당자가 6명 미만인지 확인한다.
        Integer cntAssignee = taskRepository.cntAssigneeByTaskId(ta.getTaskId());
        if (LIMIT_ASSIGN.equals(cntAssignee)) {
            return ResponseEntity.badRequest().body("담당자는 6명을 초과할 수 없습니다.");
        }

        List<TaskAssignment> taList = new ArrayList<>();
        taList.add(ta);

        taskRepository.assign(taList);
        taskRepository.log(th);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /*
     * created : 2024-07-16
     * param   : TaskAssignment, TaskHistory
     * return  : void
     * explain : 할 일 수정: 담당자 삭제
     * */
    @Transactional
    public int unassign(TaskAssignment ta, TaskHistory th){

        // Transactional을 붙여줬으므로 아래와 같이 하지 않아도 되긴 하다..
        int result = taskRepository.delAssignee(ta);
        if(result == 1) {
            taskRepository.log(th);
        }
        return result;
    }

    /*
     * created : 2024-08-01
     * param   : Long taskId
     * return  : List<TaskHistory>
     * explain : 할 일 상세: history(진행내역) 조회
     * */
    public List<TaskFile> getTaskFile(Long taskId) {
        List<TaskFile> files = taskRepository.getTaskFile(taskId);
        if(!files.isEmpty()) {
            for (TaskFile file : files) {
                file.setFormattedFileSize(TaskDetailResponse.fileSizeFormatter(file.getFileSize()));
            }
        }
        return files;
    }

    /*
     * created : 2024-08-02
     * param   :
     * return  :
     * explain : 할 일 수정: 파일 추가
     * */
    public ResponseEntity<?> attachFile(TaskDetailRequest tdr) {
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanoSec = nowWithNano.getNano();
        LocalDateTime createdAt = nowWithNano.minusNanos(nanoSec);

        ResponseEntity<?> result = null;
        try {
            List<TaskFile> taskFiles = fileStore.storeFile(tdr.getTaskFiles(), tdr.getProjectId(), tdr.getTaskId(), tdr.getAuthorMid(), createdAt);
            Long fileId = taskRepository.attachFile(taskFiles);
            log.info("insert한 후에 id를 줘야지!: {}", fileId);
            result = ResponseEntity.ok().body(fileId);
        } catch (IOException e) {
            log.info("파일 저장 에러: {}", e.getMessage());
            result = ResponseEntity.badRequest().body("파일 저장이 완료되지 않았습니다.");
        }
        return result;
    }

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 수정: 파일 삭제
     * */

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 삭제
     * */

    /*
     * created : 2024-07-18
     * param   : Long taskId
     * return  : TaskDetailResponse
     * explain : 할 일 상세
     * */
    public void getTaskDetail(Long taskId) {
        taskRepository.findByTaskId(taskId);
    }


    /*
     * created : 2024-07-19
     * param   : TaskComment
     * return  : int
     * explain : 할 일 상세: 소통하기 글 등록
     * */
    public Long addTaskComment(TaskComment taskComment) {

        // 1. comment 등록 후,
//        Long commentId = taskRepository.addComment(taskComment);

        // 2. 모두확인요청일 경우,
        /*
        if(taskComment.getType().equals("notice")) {
            // 2-1. comment id를 가지고 check_comment 테이블에도 작성자는 이미 확인한 것으로 입력한다.
            CheckComment chkCommentAuthor = CheckComment.builder()
                    .commentId(commentId)
                    .memberId(taskComment.getAuthorMid())
                    .checked(true)
                    .build();
            taskRepository.saveCheckComment(chkCommentAuthor);

            // 2-2. 해당 task의 assignees들은 확인하지 않은 것으로 check_comment 테이블에 저장한다.
            // 1) 해당 task의 assignee 목록을 가져온다.
            // 할 일의 작성자도 포함해야 하며, 소통하기의 작성자는 중복저장되지 않도록 해야 한다.
            // 단, comment id에 멤버id는 중복되어 등록되어선 안된다. (일단 db에 두 컬럼을 unique 설정해두었으나
            // 로직에서도 검사하도록 하는 게 필..요?

            List<TaskAssignment> assigneeList = taskRepository.getAssigneeList(taskComment.getTaskId());

            for(int i = 0; i < assigneeList.size(); i++) {
                CheckComment chkComment = CheckComment.builder()
                        .commentId(commentId)
                        .memberId(assigneeList.get(i).getMemberId())
                        .checked(false)
                        .build();
                taskRepository.saveCheckComment(chkComment);
            }
        }
        */
        return taskRepository.addComment(taskComment);
    }

    /*
     * created : 2024-07-19
     * param   : Long taskId
     * return  : List<TaskComment>
     * explain : 할 일 상세: 소통하기 글 조회
     * */
    public List<TaskComment> getTaskComment(Long taskId) {
        return taskRepository.getTaskComment(taskId);
    }

    /*
     * created : 2024-07-31
     * param   : TaskComment
     * return  : Integer
     * explain : 할 일 상세: 소통하기 글 수정
     * */
    public Integer editTaskComment(TaskComment editComment) {
        return taskRepository.editTaskComment(editComment);
    }

    /*
     * created : 2024-08-01
     * param   : Long taskId
     * return  : List<TaskHistory>
     * explain : 할 일 상세: history(진행내역) 조회
     * */
    public List<TaskHistory> getTaskHistory(Long taskId){
        return taskRepository.getTaskHistory(taskId);
    }
} // class TaskService ends
