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

    // 담당자 배정
    int assign(List<TaskAssignment> taskAssignment);

    // 담당자 삭제
    int delAssignee(TaskAssignment taskAssignment);

    // 할 일: 파일 첨부
    void attachFile(List<TaskFile> taskFile);
    
    // 할 일: 파일 삭제

    // 할 일 수정: 할일명, 진행상태, 마감일, 중요도
    Integer editTaskStatus(TaskEditRequest ter);

    // 할 일 상세
    Optional<OnTrackTask> findByTaskId(Long taskId);

    // 할 일 상세: 소통하기 글 등록
    Long addComment(TaskComment taskComment);

    // 할 일 상세: 모두확인요청한 소통글의 확인여부 등록
    Long saveCheckComment(CheckComment chkComment);

    // 할 일 상세: 소통하기 글 목록
    List<TaskComment> getTaskComment(Long TaskId);

    // 각 할 일 담당자 목록
    List<TaskAssignment> getAssigneeList(Long taskId);

    // 해당 멤버의 할 일
    List<TaskList> findTaskByMemberId(Long memberId);

    // Assignee view: 담당자별 할 일 목록
    List<AssigneeTaskList> getAssigneeView(Long memberId);

    // Status view: 진행상태별 할 일 목록
    List<StatusTaskList> getStatusView(StatusViewRequest statusViewRequest);

    // 담당자 없는 할 일 목록
    List<NoAssigneeTask> getNoAssigneeTask(Long projectId);

    // 해당 할 일에 배정된 담당자 수
    Integer cntAssigneeByTaskId(Long taskId);

    // 해당 할 일에 이미 배정된 담당자인지 확인
    Long chkAssigned(TaskAssignment ta);

}
