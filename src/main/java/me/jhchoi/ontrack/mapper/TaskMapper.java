package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.*;
import me.jhchoi.ontrack.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {
    
    // 새 할 일 등록
    Long newTask(OnTrackTask task);

    // 기록(history): 새 할 일 등록(할일명), 담당자 배정,
    Long log(TaskHistory taskHistory);

    // 할 일 조회
    Optional<OnTrackTask> findByTaskId(Long taskId);
    
    // 담당자 배정
    int assign(List<TaskAssignment> taskAssignment);

    // 담당자 삭제
    int delAssignee(TaskAssignment taskAssignment);

    // 할 일: 파일 첨부
    Long attachFile(List<TaskFile> taskFile);

    // 할 일: (작성자에 의한) 파일 삭제
    int delFile(Long fileId);

    // 할 일: (관리자에 의한) 파일 삭제
    int deleteFileByAdmin(TaskFile deleteItem);

    // 할 일 수정: 할일명
    Integer editTaskTitle(TaskEditRequest ter);

    // 할 일 수정: 진행상태 수정
    Integer editTaskStatus(TaskEditRequest ter);

    // 할 일 수정; 마감일 수정
    Integer editTaskDueDate(TaskEditRequest ter);
    

    // 할 일 상세: 소통하기 글 등록
    Long addComment(TaskComment taskComment);

    // 할 일 상세: 소통하기 글 수정
    Integer editTaskComment(TaskComment editComment);

    // 할 일 상세: 소통하기 글 목록 조회
    List<TaskComment> getTaskComment(Long TaskId);

    // 각 할 일 담당자 목록
    List<TaskAssignment> getAssigneeList(Long taskId);

    // 해당 멤버의 할 일
    List<TaskAndAssignee> findTaskByMemberId(Long memberId);

    // Assignee view: 담당자별 할 일 목록
    List<TaskAndAssignee> getAssigneeView(Long memberId);

    // Status view: 진행상태별 할 일 목록
    List<TaskAndAssignee> getStatusView(TaskAndAssignee statusViewRequest);

    // 담당자 없는 할 일 목록
    List<TaskAndAssignee> getNoAssigneeTask(Long projectId);

    // 해당 할 일에 배정된 담당자 수
    Integer cntAssigneeByTaskId(Long taskId);

    // 해당 할 일에 이미 배정된 담당자인지 확인
    Long chkAssigned(TaskAssignment ta);

    // 할 일 상세: history 조회
    List<TaskHistory> getTaskHistory(Long taskId);

    // 할 일 상세: 파일 조회
    List<TaskFile> getTaskFile(Long taskId);

    TaskFile findFileById(Long fildId);

}
