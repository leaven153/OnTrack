package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.AssigneeTaskList;
import me.jhchoi.ontrack.dto.AssignmentList;
import me.jhchoi.ontrack.dto.TaskList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {
    
    // 새 할 일 등록
    void newTask(OnTrackTask task);

    // 기록(history): 새 할 일 등록(할일명), 담당자 배정,
    void log(TaskHistory taskHistory);

    // 담당자 배정
    void assign(List<TaskAssignment> taskAssignment);

    // 담당자 삭제
    void unassign(TaskAssignment taskAssignment);

    // 할 일: 파일 첨부
    
    // 할 일: 파일 삭제

    // 할 일 수정: 할일명, 진행상태, 마감일, 중요도
    void update(@Param("taskId") Long taskId, @Param("modTask")OnTrackTask task);

    // 할 일 상세
    Optional<OnTrackTask> findByTaskId(Long taskId);

    // 각 할 일 담당자 목록
    List<TaskAssignment> getAssigneeList(Long taskId);

//    List<TaskList> findByMemberId(Long memberId);

    List<AssigneeTaskList> getAssigneeView(Long memberId);

}
