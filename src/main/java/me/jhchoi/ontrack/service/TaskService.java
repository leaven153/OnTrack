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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final FileStore fileStore;

    /**
     * created  : 2024-05-14
     * updated  : 2024-05-23, 24
     * param    : TaskAndAssignee taskFormRequest
     * return   : Long
     * explain  : 새 할 일 등록 (할 일 정보, 담당자(nullable), 파일(nullable))
     * */
    @Transactional
    public Long addTask(TaskAndAssignee taskFormRequest) {

        projectRepository.findByProjectId(taskFormRequest.getProjectId());
        // 1. 할 일 객체 생성
        OnTrackTask task = taskFormRequest.dtoToEntityTask();

        // 1-1. 할 일 등록
        taskRepository.addTask(task);

        // 2. history 등록 - ①할 일 명,
        taskRepository.log(TaskHistory.logNewTask(task));
        
        // 3. 담당자 유무 확인
        if (taskFormRequest.getAssigneeMids() != null && !taskFormRequest.getAssigneeMids().isEmpty()) {

            /*
            taskFormRequest.setAssigneeNames(new ArrayList<>());
            // 3-1. 담당자 nickname 가져오기
            for(int i= 0; i < taskFormRequest.getAssigneeMids().size(); i++){
                log.info("task service에서 멤버 id: {}", taskFormRequest.getAssigneeMids().get(i));
                List<MemberInfo> mList = projectRepository.getMemberInfo(MemberInfo.builder().projectId(taskFormRequest.getProjectId()).memberId(taskFormRequest.getAssigneds().get(i)).build());
                taskFormRequest.getAssigneeNames().add(mList.get(0).getNickname());
            }
            */
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
                /*
                  C:\Users/user\AppData\Local\Temp\tomcat.8080.2139694289704046896\work\Tomcat\localhost\ROOT\9\14\9bba9073-0957-4dd9-acb4-0e3103105f27.txt (지정된 경로를 찾을 수 없습니다)
                  */
            }
        }

        // 5. 마감일 유무 확인 후 history insert
        if(taskFormRequest.getTaskDueDate() != null) {
            taskRepository.log(TaskHistory.logDueDate(task));
        }

        return task.getId();
    }



    /**
     * created : 2024-07-31
     * param   : TaskHistory, TaskEditRequest
     * return  : ResponseEntity.ok().body(들어온할일명)/ResponseEntity.badRequest().body(ErrorResponse)
     * explain : 할 일 수정: 할 일 명
     * */
    @Transactional
    public ResponseEntity<?> editTaskTitle(TaskHistory th, TaskEditRequest ter){
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(ter.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            taskRepository.log(th);
            Integer result = taskRepository.editTaskTitle(ter);
            if (result != 1){
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("수정이 완료되지 않았습니다.").taskRemoved(false).build());
            }
            return ResponseEntity.ok().body(ter.getTitle());
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());

    }

    /**
     * created : 2024-07-29
     * param   : TaskHistory, TaskEditRequest
     * return  : ResponseEntity.ok().body(들어온진행상태값)/ResponseEntity.badRequest().body(ErrorResponse)
     * explain : 할 일 수정: 진행상태
     * */
    @Transactional
    public ResponseEntity<?> editTaskStatus(TaskHistory th, TaskEditRequest ter){
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(ter.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            // 진행상태는 담당자가 없을 경우, 시작 안함 상태가 될 수 없다.
            Integer assignedNum = taskRepository.cntAssigneeByTaskId(ter.getTaskId());
            if(assignedNum != null) {
                taskRepository.log(th);
                Integer result = taskRepository.editTaskStatus(ter);
                if(result == 1) {
                    return ResponseEntity.ok().body(ter.getStatus());
                } else {
                    return ResponseEntity.badRequest().body(ErrorResponse.builder().message("진행상태 변경이 완료되지 않았습니다.").taskRemoved(false).build());
                }
            } else {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("담당자가 없는 할 일은 진행상태를 바꿀 수 없습니다.").taskRemoved(false).build());
            }
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());

    }

    /**
     * created : 2024-07-30
     * param   : TaskHistory, TaskEditRequest
     * return  : ResponseEntity.ok().body("")/ResponseEntity.badRequest().body(ErrorResponse)
     * explain : 할 일 수정: 마감일
     * */
    @Transactional
    public ResponseEntity<?> editTaskDueDate(TaskHistory th, TaskEditRequest ter){
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(ter.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            taskRepository.log(th);
            Integer result = taskRepository.editTaskDueDate(ter);
            if(result == 1) {
                return ResponseEntity.ok().body("");
            } else {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("마감일 변경이 완료되지 않았습니다.").taskRemoved(false).build());
            }
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }

    /**
     * created : 2024-06-18
     * param   : TaskAssignment, TaskHistory
     * return  : ResponseEntity.ok("")/ResponseEntity.badRequest().body(ErrorResponse)
     * explain : 할 일 수정: 담당자 추가
     */
    @Transactional
    public ResponseEntity<?> addAssignee(TaskAssignment ta, TaskHistory th){

        // 예외는 어떻게 처리해야 할까?
        // 이미 삭제된 task 혹은 project에 담당자를 배정하려고 하는 경우?
        // assign에는 그닥 오류가 없을 수 있다. 없는 task, project라 해도
        // 이를 test하고 저장하지 않으니까...

        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(ta.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            // 해당 일에 이미 배정(참여)된 담당자인지 확인 필요
            Long assigned = taskRepository.chkAssigned(ta);
            if (ta.getTaskId().equals(assigned)) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("이미 배정된 담당자입니다.").taskRemoved(false).build());
            }

            Integer LIMIT_ASSIGN = 6;
            // 담당자가 6명 미만인지 확인한다.
            Integer cntAssignee = taskRepository.cntAssigneeByTaskId(ta.getTaskId());
            if (LIMIT_ASSIGN.equals(cntAssignee)) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("담당자는 6명을 초과할 수 없습니다.").taskRemoved(false).build());
            }

            List<TaskAssignment> taList = new ArrayList<>();
            taList.add(ta);

            taskRepository.assign(taList);
            taskRepository.log(th);

            return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }


    /**
     * created : 2024-07-16
     * param   : TaskAssignment, TaskHistory
     * return  : ResponseEntity.ok("") / ResponseEntity.badRequest().body(ErrorResponse)
     * explain : 할 일 수정: 담당자 삭제
     * */
    @Transactional
    public ResponseEntity<?> unassign(TaskAssignment ta, TaskHistory th){

        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(ta.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            // Transactional은 어떻게 되는가
            int result = taskRepository.delAssignee(ta);
            if(result == 1) {
                taskRepository.log(th);
                return ResponseEntity.ok("");
            }
            return ResponseEntity.badRequest().body(ErrorResponse.builder().message("담당자 삭제가 완료되지 않았습니다.").taskRemoved(false).build());
        }

        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }

    /**
     * created : 2024-08-01
     * param   : Long taskId
     * return  : List<TaskFile>
     * explain : 할 일 상세: 파일 조회
     * */
    public List<TaskFile> getTaskFile(Long taskId) {
        List<TaskFile> files = taskRepository.getTaskFile(taskId);
        if(!files.isEmpty()) {
            for (TaskFile file : files) {
                file.setFormattedFileSize(FileStore.fileSizeFormatter(file.getFileSize()));
            }
        }
        return files;
    }

    /**
     * created : 2024-08-02
     * param   : TaskDetailRequest
     * return  : ResponseEntity
     * explain : 할 일 수정: 파일 추가
     * */
    @Transactional
    public ResponseEntity<?> attachFile(TaskDetailRequest tdr) {
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(tdr.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            LocalDateTime nowWithNano = LocalDateTime.now();
            int nanoSec = nowWithNano.getNano();
            LocalDateTime createdAt = nowWithNano.minusNanos(nanoSec);

            ResponseEntity<?> result = null;
            try {
                List<TaskFile> taskFiles = fileStore.storeFile(tdr.getTaskFiles(), tdr.getProjectId(), tdr.getTaskId(), tdr.getAuthorMid(), createdAt);
                int fileAttachResult = taskRepository.attachFile(taskFiles);
//            log.info("파일 저장 후, dto에 fileId 담겼나요?: {}", taskFiles);
                if(fileAttachResult == 1) {
                    result = ResponseEntity.ok().body(taskFiles);
                }
            } catch (IOException e) {
                log.info("파일 저장 에러: {}", e.getMessage());
                result = ResponseEntity.badRequest().body(ErrorResponse.builder().message("파일 저장이 완료되지 않았습니다.").taskRemoved(false).build());
            }
            return result;
        }

        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }

    /**
     * created : 2024-08-05
     * param   : Long fileId
     * return  : ResponseEntity
     * explain : 할 일 수정: (작성자에 의한) 파일 삭제
     * */
    @Transactional
    public ResponseEntity<?> delFile(Long fileId) {

        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Long taskId = taskRepository.findTaskIdByFileId(fileId);
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(taskId);
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){

            // 실제 파일 삭제 후
            TaskFile file = taskRepository.findFileById(fileId);
            String path = Paths.get(file.getFilePath(), file.getFileNewName()).toString();
            File delFile = new File(path);

            Boolean deleted = false;

            if(delFile.exists()){
                deleted = delFile.delete();
            } else {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("존재하지 않는 파일입니다.").taskRemoved(false).build());
            }

            // DB 기록 삭제
            int dbResult = 0;

            if(deleted){
                dbResult = taskRepository.delFile(fileId);
            }

            if(!deleted || dbResult != 1) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("파일 삭제가 완료되지 않았습니다.").taskRemoved(false).build());
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());

    }

    /**
     * created : 2024-08-05
     * param   : Long fileId, Long executorMid
     * return  : ResponseEntity
     * explain : 할 일 수정: (관리자에 의한) 파일 삭제
     * */
    @Transactional
    public ResponseEntity<?> deleteFileByAdmin(TaskFile deleteItem){
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Long taskId = taskRepository.findTaskIdByFileId(deleteItem.getId());
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(taskId);
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){


            // 실제 파일 삭제 후
            TaskFile file = taskRepository.findFileById(deleteItem.getId());
            String path = Paths.get(file.getFilePath(), file.getFileNewName()).toString();
            File delFile = new File(path);
            Boolean deleted = false;
            if(delFile.exists()){
                deleted = delFile.delete();
            } else {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("존재하지 않는 파일입니다.").taskRemoved(false).build());
            }

            // DB 기록 수정
            int result = 0;
            if(deleted) result = taskRepository.deleteFileByAdmin(deleteItem);

            if(!deleted || result != 1){
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("파일 삭제가 완료되지 않았습니다.").taskRemoved(false).build());
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }


    /**
     * created : 2024-07-19
     * param   : TaskComment
     * return  : ResponseEntity
     * explain : 할 일 상세: 소통하기 글 등록
     * */
    @Transactional
    public ResponseEntity<?> addTaskComment(TaskComment taskComment) {

        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(taskComment.getTaskId());

        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            // 1. comment 등록 후,
            log.info("등록 전 taskCommentId는 null:{}", taskComment);
            int result = taskRepository.addComment(taskComment);
            if(result != 1) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("소통하기 글 내용이 등록되지 않았습니다.").taskRemoved(true).build());
            }
            return ResponseEntity.ok(taskComment.getId());
        }
        log.info("등록 후 taskComment:{}", taskComment);
        log.info("등록 후 taskCommentId는:{}", taskComment.getId());


        // 2. notice(중요;모두확인요청)일 경우,
        /*
        if(taskComment.getType().equals("notice")) {
            // 2-1. comment id를 가지고 check_comment 테이블에도 작성자는 이미 확인한 것으로 입력한다.
            CheckComment chkCommentAuthor = CheckComment.builder()
                    .taskId(taskComment.getTaskId())
                    .commentId(taskComment.getId())
                    .memberId(taskComment.getAuthorMid())
                    .userId(memberRepository.findByMemberId(taskComment.getAuthorMid()).getUserId())
                    .checked(true)
                    .build();
            taskRepository.registerCheckComment(chkCommentAuthor);

            // 2-2. 해당 task의 assignees들은 확인하지 않은 것으로 check_comment 테이블에 저장한다.
            // 1) 해당 task의 assignee 목록을 가져온다.
            // 할 일의 작성자도 포함해야 하며, 소통하기의 작성자는 중복저장되지 않도록 해야 한다.
            // 단, comment id에 멤버id는 중복되어 등록되어선 안된다. (일단 db에 두 컬럼을 unique 설정해두었으나
            // 로직에서도 검사하도록 하는 게 필..요?

            List<TaskAssignment> assigneeList = taskRepository.getAssigneeList(taskComment.getTaskId());

            for (TaskAssignment taskAssignment : assigneeList) {
                if (!Objects.equals(taskAssignment.getMemberId(), taskComment.getAuthorMid())) {
                    CheckComment chkComment = CheckComment.builder()
                            .taskId(taskComment.getTaskId())
                            .commentId(taskComment.getId())
                            .memberId(taskAssignment.getMemberId())
                            .userId(memberRepository.findByMemberId(taskAssignment.getMemberId()).getUserId())
                            .checked(false)
                            .build();
                    taskRepository.registerCheckComment(chkComment);
                }
            }

        } */

        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }

    /**
     * created : 2024-07-19
     * param   : Long taskId
     * return  : List<TaskComment>
     * explain : 할 일 상세: 소통하기 글 조회
     * */
    public List<TaskComment> getTaskComment(Long taskId) {
        return taskRepository.getTaskComment(taskId);
    }

    /**
     * created : 2024-08-
     * param   : CheckComment
     * return  : List<CheckComment>
     * explain : 할 일 상세: 중요 소통글 확인 여부 조회
     * */
    public CheckComment getCheckComment(CheckComment cc) {
        log.info("서비스에서의 결과: {}", taskRepository.getCheckComment(cc));
        return taskRepository.getCheckComment(cc);
    }

    /**
     * created : 2024-08-19
     * param: Long commentId
     * return: Map<해당 comment의 task id와 확인하지 않은 유저 id List>
     * explain: 중요 소통글 등록 시 알림 출력할 대상 조회(웹소켓)
     * */
    public Map<Long, List<Long>> alarmNoticeComment(Long commentId) {
        List<CheckComment> unchechkedList = taskRepository.findUncheckedCommentByCommentId(commentId);
        Map<Long, List<Long>> taskIdAndUserList = new HashMap<>();

        List<Long> userIdList = new ArrayList<>();
        if(!unchechkedList.isEmpty()){
            Long taskId = unchechkedList.get(0).getTaskId();
            for (CheckComment checkComment : unchechkedList) {
                userIdList.add(checkComment.getUserId());
            }
            taskIdAndUserList.put(taskId, userIdList);
        }

        log.info("task id and user id list 전송: {}", taskIdAndUserList);

        return taskIdAndUserList;
    }


    /**
     * created : 2024-07-31
     * param   : TaskComment
     * return  : ResponseEntity<String>
     * explain : 할 일 상세: 소통하기 글 수정
     * */
    @Transactional
    public ResponseEntity<?> editTaskComment(TaskComment editComment) {
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(editComment.getTaskId());
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            TaskComment chkExists = taskRepository.findCommentById(editComment.getId());
            ResponseEntity<?> response = ResponseEntity.ok("");
            if(chkExists == null) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 글이 존재하지 않습니다.").taskRemoved(false).build());
            }
            Integer result = taskRepository.editTaskComment(editComment);
            if(result != 1) {
                response = ResponseEntity.badRequest().body(ErrorResponse.builder().message("글 수정이 완료되지 않았습니다.").taskRemoved(false).build());
            }
            return response;
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }

    /**
     * created : 2024-08-06
     * param   : TaskDetailRequest
     * return  : ResonseEntity<Void>
     * explain : 할 일 상세: 작성자에 의한 소통하기 글 삭제
     * */
    public ResponseEntity<?> delComment(Long commentId) {
        Long taskId = taskRepository.findTaskIdByCommentId(commentId);
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(taskId);
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            Integer result = taskRepository.delComment(commentId);
            if(result != 1){
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 소통글이 존재하지 않습니다.").taskRemoved(false).build());
            }
            return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }


    /**
     * created : 2024-08-06
     * param   : TaskDetailRequest
     * return  : ResponseEntity<String>
     * explain : 할 일 상세: 관리자에 의한 소통하기 글 차단 (DB에는 남아있음. 관리자 화면에서 조회 가능)
     * */
    @Transactional
    public ResponseEntity<?> blockTaskComment(TaskDetailRequest blockComment){
        Long taskId = taskRepository.findTaskIdByCommentId(blockComment.getCommentId());
        // 해당 task가 휴지통으로 이동되었는지 먼저 확인한다.
        Optional<OnTrackTask> taskExist = taskRepository.findByTaskId(taskId);
        if(taskExist.isPresent() && taskExist.get().getDeletedBy() == null){
            TaskComment chkExists = taskRepository.findCommentById(blockComment.getCommentId());

            if(chkExists == null) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 글이 존재하지 않습니다.").taskRemoved(false).build());
            }
            Integer result = taskRepository.blockComment(blockComment);
            if(result != 1) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().message("글 삭제가 완료되지 않았습니다.").taskRemoved(false).build());
            }
            return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message("해당 할 일이 존재하지 않습니다.").taskRemoved(true).build());
    }


    /**
     * created : 2024-08-01
     * param   : Long taskId
     * return  : List<TaskHistory>
     * explain : 할 일 상세: history(진행내역) 조회
     * */
    public List<TaskHistory> getTaskHistory(Long taskId){
        return taskRepository.getTaskHistory(taskId);
    }


    /**
     * created : 2024-08-
     * param   : BinRequest
     * return  : ResponseEntity
     * explain : 할 일 삭제/복원(update deletedAt, deletedBy)
     * */
    @Transactional
    public void moveToBin(BinRequest binRequest) {

        // remove(프로젝트 → 휴지통): 한 개의 projectId, n개의 taskIds(List<Long>)
        for(int i = 0; i < binRequest.getTaskIds().size(); i++){

            // 이미 삭제된 할 일인지 확인한다.
            Optional<OnTrackTask> chk = taskRepository.findByTaskId(binRequest.getTaskIds().get(i));
            if(chk.isPresent() && chk.get().getDeletedBy() == null) {

                // ontrack_task 컬럼 상태 update
                OnTrackTask task = OnTrackTask.builder()
                        .id(binRequest.getTaskIds().get(i))
                        .deletedAt(binRequest.getDeletedAt())
                        .deletedBy(binRequest.getDeletedBy())
                        .build();
                taskRepository.taskSwitchBin(task);

                // history남긴다.
                TaskHistory th = TaskHistory.builder()
                        .projectId(binRequest.getProjectId())
                        .taskId(binRequest.getTaskIds().get(i))
                        .modItem("할 일")
                        .modType("삭제") // 변경
                        .modContent("프로젝트에서") // 프로젝트에서 휴지통으로 이동
                        .updatedAt(binRequest.getDeletedAt())
                        .updatedBy(binRequest.getDeletedBy())
                        .build();
                taskRepository.log(th);
            }
        }
    }

    /**
     * created : 2024-08-23
     * param   : Long taskId
     * return  : Map<Long, List<ProjectMember>>
     * explain : 할 일 삭제시, 알람(웹소켓) 보낼 해당 할 일의 담당자들 user id 조회
     * */
    public Map<Long, List<Long>> alarmBin(List<Long> taskIds){
        // 해당 task의 담당자들의 userId를 찾는다.
        Map<Long, List<Long>> taskIdAndAssigneeUserId = new LinkedHashMap<>();
        for (Long taskId : taskIds) {
            List<Long> userId = taskRepository.findUserByTaskIdForAlarm(taskId);
            taskIdAndAssigneeUserId.put(taskId, userId);
        }
        return taskIdAndAssigneeUserId;
    }

    /**
     * created : 2024-08
     * param   : Long taskId
     * return  : BinResponse
     * explain : 휴지통에 접속 중인 유저에게 방금 삭제된 담당 할 일 동적 출력(웹소켓)
     * */
    public BinResponse binTaskRow(Long taskId) {
        return taskRepository.binTaskRow(taskId);
    }

    /**
     * created : 2024-08-
     * param   : BinRequest
     * return  : ResponseEntity
     * explain : 할 일 복원(휴지통 → 프로젝트)
     * */
    public ResponseEntity<String> restoreTask(BinRequest binRequest) {
        ResponseEntity<String> response = ResponseEntity.ok("할 일 복원이 완료되었습니다.");
        // restore(휴지통 → 프로젝트): projectAndTaskId(List<Map<String, Long>>)
        // 1. history 남긴다.
        for(int i = 0; i < binRequest.getProjectAndTaskId().size(); i++) {
            TaskHistory th = TaskHistory.builder()
                    .projectId(binRequest.getProjectAndTaskId().get(i).get("projectId"))
                    .taskId(binRequest.getProjectAndTaskId().get(i).get("taskId"))
                    .modItem("할 일")
                    .modType("복원") // 변경
                    .modContent("휴지통에서 프로젝트로") // 이동
                    .updatedBy(binRequest.getRestoredBy())
                    .updatedAt(binRequest.getRestoredAt())
                    .build();
            Long result = taskRepository.log(th);
            if (result != 1) {
                response = ResponseEntity.internalServerError().body("복원 기록이 완료되지 않았습니다.");
            }
        }

        // 2. ontrack_task 컬럼 상태 update
        for(int i = 0; i < binRequest.getProjectAndTaskId().size(); i++){
            OnTrackTask task = OnTrackTask.builder()
                    .id(binRequest.getProjectAndTaskId().get(i).get("taskId"))
                    .projectId(binRequest.getProjectAndTaskId().get(i).get("projectId"))
                    .deletedBy(null)
                    .deletedAt(null)
                    .build();
            Long result = taskRepository.taskSwitchBin(task);
            if(result != 1) {
                response = ResponseEntity.internalServerError().body("복원이 완료되지 않았습니다.");
            }
        }

        return response;
    }

    /**
     * created : 2024-08-
     * param   :
     * return  : ResponseEntity
     * explain : 휴지통 조회(at My page. cf. 관리자가 프로젝트 내 휴지통 조회)
     * */
    public List<BinResponse> getMyBin(Long userId) {

        List<BinResponse> binTaskList = new ArrayList<>();

        // 1. project_member: 내 userId의 memberId, projectId
        List<ProjectMember> memberInfo = memberRepository.findByUserId(userId);

        log.info("서비스에서 찾아낸 memberInfo: {}", memberInfo);

        // 소속된 프로젝트가 없다면 빈 리스트를 바로 보낸다.
        if(memberInfo.isEmpty()) {
            log.info("소속된 프로젝트가 없을 수도 있지");
            return binTaskList;
        }

        List<BinResponse> result = new ArrayList<>();
        // 2-1. 내가 담당자이거나 작성자인 할 일 중, 삭제일이 7일 미만 전인 할 일
        for (ProjectMember projectMember : memberInfo) {
            binTaskList = taskRepository.getBin(projectMember.getId());

            for (BinResponse binResponse : binTaskList) {
                // 2-2. 해당 일마다 내 member id 입력
                binResponse.setMemberId(projectMember.getId());
                // 2-3. 프로젝트명 입력
                binResponse.setProjectName(projectRepository.findByProjectId(projectMember.getProjectId()).getProjectName());

                // 2-4. 삭제한 멤버의 이름 입력
                binResponse.setDeleterName(memberRepository.findByMemberId(binResponse.getDeletedBy()).getNickname());

                // 2-5. 내가 작성자인 할 일에는 authorized에 true값 입력
                if (Objects.equals(binResponse.getAuthorMid(), projectMember.getId())) {
                    binResponse.setAuthorized(true);
                }
            }

            result.addAll(binTaskList);
            /*
             * 서비스에서 찾아낸 첫번째 쿼리, getBin: [BinResponse(projectId=9, projectName=null,
             * taskId=20, memberId=null, taskTitle=고독한 산책자의 몽상, deletedBy=14,
             * deleterName=null, deletedAt=2024-08-12T18:35:01, authorMid=14, authorized=null),
             * BinResponse(projectId=9, projectName=null, taskId=33, memberId=null,
             * taskTitle=완성할 수 있을까, deletedBy=14, deleterName=null,
             * deletedAt=2024-08-12T18:43:45, authorMid=14, authorized=null)]*/
        }

        log.info("서비스에서 넘기는 result: {}", result);

        return result;
    }

    /**
     * created : 2024-08-
     * param   :
     * return  : ResponseEntity
     * explain : (휴지통에서) 영구삭제
     * */
    @Transactional
    public ResponseEntity<?> deleteTask(Long taskId) {

        // 할 일 영구 삭제 ⓣ 담당자 삭제: task_assignment, taskId
        // 할 일 영구 삭제 ② 소통 내역 삭제: task_comment, taskId
        // 할 일 영구 삭제 ③ 진행 내역 삭제 : task_history, taskId
        // (영구 삭제 기록은 어떻게 할 것인가...? → task id가 없는데 존재하는 진행내역이 바람직한가?)
        // 할 일 영구 삭제 ④ 파일 삭제: task_file, taskId
        // 할 일 영구 삭제 ⑤ ontrack_task에서 삭제

        return ResponseEntity.ok("할 일 영구삭제 중");
    }

} // class TaskService ends
