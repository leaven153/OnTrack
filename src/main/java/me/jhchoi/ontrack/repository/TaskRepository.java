package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.TasksResponse;
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
    public Long newTask(OnTrackTask task){
        taskMapper.newTask(task);
        return task.getId();
    }

    // 기록(history): 새 할 일 등록
    public void log(TaskHistory taskHistory){
        taskMapper.log(taskHistory);
    }

    // 할 일 담당자 등록
    public void assign(List<TaskAssignment> assignees){
        taskMapper.assign(assignees);
    }

    // 할 일 상세
    public Optional<OnTrackTask> findById(@Param("taskId") Long taskId){
        return taskMapper.findById(taskId);
    }


    // 할 일 목록
    public List<TasksResponse> allTasksInProject(Long projectId){
        return taskMapper.findAll();
    }
}
