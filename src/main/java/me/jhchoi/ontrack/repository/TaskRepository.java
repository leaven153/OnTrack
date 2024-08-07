package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.*;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.mapper.TaskMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskMapper taskMapper;

    // 새 할 일 등록
    public Long addTask(OnTrackTask task){
        return taskMapper.addTask(task);
    }

    // 기록(history): 새 할 일 등록, 담당자 추가
    public Long log(TaskHistory taskHistory){
        return taskMapper.log(taskHistory);
    }

    // 할 일 담당자 등록
    public int assign(List<TaskAssignment> assignees){
        return taskMapper.assign(assignees);
    }

    // 할 일 담당자 해제
    public int delAssignee(TaskAssignment ta) { return taskMapper.delAssignee(ta); }

    // 할 일 별 첨부파일 등록
    public int attachFile(List<TaskFile> taskFile) { return taskMapper.attachFile(taskFile); }

    // 할 일 상세 조회
    public Optional<OnTrackTask> findByTaskId(@Param("taskId") Long taskId){
        return taskMapper.findByTaskId(taskId);
    }

    // 각 할 일 담당자 목록
    public List<TaskAssignment> getAssigneeList(Long taskId) { return taskMapper.getAssigneeList(taskId); }

    // Assignee view: 담당자별 할 일 목록
    public List<TaskAndAssignee> getAssigneeView(Long memberId) { return taskMapper.getAssigneeView(memberId); }

    // Assignee view: 담당자 없는 할 일 목록
    public List<TaskAndAssignee> getNoAssigneeTask(Long projectId) { return taskMapper.getNoAssigneeTask(projectId); }

    // Status view: 진행상태별 할 일 목록
    public List<TaskAndAssignee> getStatusView(TaskAndAssignee statusViewRequest) { return taskMapper.getStatusView(statusViewRequest); }

    // 할 일 수정: 할 일 명
    public Integer editTaskTitle(TaskEditRequest ter) { return taskMapper.editTaskTitle(ter); }

    // 할 일 수정: 진행상태
    public Integer editTaskStatus(TaskEditRequest ter) { return taskMapper.editTaskStatus(ter); }

    // 할 일 수정: 마감일
    public Integer editTaskDueDate(TaskEditRequest ter) { return taskMapper.editTaskDueDate(ter); }

    // 해당 멤버의 맡은 할 일
    public List<TaskAndAssignee> findTaskByMemberId(Long memberId) { return taskMapper.findTaskByMemberId(memberId); }

    // 할 일 조회: 할 일에 배정된 담당자 수
    public Integer cntAssigneeByTaskId(Long taskId) { return taskMapper.cntAssigneeByTaskId(taskId); }

    // 해당 할 일에 이미 배정된 담당자인지 확인
    public Long chkAssigned(TaskAssignment ta) { return taskMapper.chkAssigned(ta); }

    // 할 일 상세: 소통하기 글 등록
    public int addComment(TaskComment taskComment) { return taskMapper.addComment(taskComment); }

    // 할 일 상세: 소통하기 글 목록
    public List<TaskComment> getTaskComment(Long taskId) { return taskMapper.getTaskComment(taskId); }

    // 소통하기 글 조회
    public TaskComment findCommentById(Long commentId) { return taskMapper.findCommentById(commentId); }

    // 할 일 상세: 소통하기 글 수정
    public Integer editTaskComment(TaskComment editComment) {
        return taskMapper.editTaskComment(editComment);
    }

    // 할 일 상세: (관리자에 의한) 소통하기 글 차단
    public Integer blockComment(TaskDetailRequest taskDetailRequest) { return taskMapper.blockComment(taskDetailRequest); }

    // 할 일 상세: 소통하기 글 삭제
    public Integer delComment(Long commentId) { return taskMapper.delComment(commentId); }

    // 할 일 상세: history(진행내역) 조회
    public List<TaskHistory> getTaskHistory(Long taskId){
        return taskMapper.getTaskHistory(taskId);
    }

    // 할 일 상세: file(파일) 목록 조회
    public List<TaskFile> getTaskFile(Long taskId) { return taskMapper.getTaskFile(taskId); }

    // 할 일 상세: file 다운
    public TaskFile findFileById(Long fileId) { return taskMapper.findFileById(fileId); }

    // 할 일 상세: (작성자에 의한)file 삭제
    public int delFile(Long fileId) { return taskMapper.delFile(fileId); }

    // 할 일 상세: (관리자에 의한)file 삭제
    public int deleteFileByAdmin(TaskFile deleteItem) { return taskMapper.deleteFileByAdmin(deleteItem); }

    // 할 일 삭제
    public List<OnTrackTask> delTask(Long taskId) { return taskMapper.delTask(taskId); }

    // 휴지통으로 이동 or 복원 (update deletedAt, deletedBy)
    public Long taskBin(OnTrackTask binRequest) { return taskMapper.taskSwitchBin(binRequest); }


}
