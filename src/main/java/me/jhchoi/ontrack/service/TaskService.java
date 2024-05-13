package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.dto.TasksResponse;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    // 새 할 일 등록
    // 할 일 담당자: nullable
    // 첨부파일: nullable
    public void addTask(OnTrackTask task, List<TaskAssignment> assignees) {

        taskRepository.newTask(task);
        taskRepository.assign(assignees);

        // history 등록 - 할 일 명, 담당자

    }

    // 할 일 수정: 할일명, 중요도, 진행상태, 마감일
    
    // 할 일 수정: 담당자 추가
    // 할 일 수정: 담당자 삭제
    // 할 일 수정: 파일 추가
    // 할 일 수정: 파일 삭제
    
    // 할 일 상세
    
    // 할 일 목록
    public List<TasksResponse> findAll(){
        return taskRepository.findAll();
    }


}
