package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.dto.TasksResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {
    
    // 새 할 일 저장
    void newTask(OnTrackTask task);

    // 담당자 배정
    void assign(List<TaskAssignment> taskAssignment);

    // 담당자 삭제
    void unassign(TaskAssignment taskAssignment);

    // 할 일: 파일 첨부


    // 할 일 수정
    void update(@Param("taskId") Long taskId, @Param("updateParam")OnTrackTask task);

    // 할 일 상세
    Optional<OnTrackTask> findById(Long taskId);

    // 할 일 목록
    List<TasksResponse> findAll();


}
