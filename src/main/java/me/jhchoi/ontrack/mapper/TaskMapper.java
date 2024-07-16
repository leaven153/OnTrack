package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
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
//    void update(@Param("taskId") Long taskId, @Param("modTask")OnTrackTask task);

    // 할 일 상세
    Optional<OnTrackTask> findByTaskId(Long taskId);

    // 각 할 일 담당자 목록
    List<TaskAssignment> getAssigneeList(Long taskId);

    // 해당 멤버의 할 일
    List<TaskList> findTaskByMemberId(Long memberId);

    // Assignee view: 담당자별 할 일 목록
    List<AssigneeTaskList> getAssigneeView(Long memberId);

    // Status view: 진행상태별 할 일 목록
    List<StatusTaskList> getStatusView(StatusViewRequest statusViewRequest);

    List<NoAssigneeTask> getNoAssigneeTask(Long projectId);

    Integer cntAssigneeByTaskId(Long taskId);

    // 해당 할 일에 이미 배정된 담당자인지 확인
    Long chkAssigned(TaskAssignment ta);

}
