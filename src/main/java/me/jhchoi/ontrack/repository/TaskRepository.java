package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.mapper.TaskMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final TaskMapper taskMapper;

    public OnTrackTask save(OnTrackTask task){
        taskMapper.save(task);
        return task;
    }

    public Optional<OnTrackTask> findById(@Param("taskId") Long taskId){
        return taskMapper.findById(taskId);
    }
}
