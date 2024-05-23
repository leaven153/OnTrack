package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.AddTaskRequest;
import me.jhchoi.ontrack.dto.TasksResponse;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * created  : 2024-05-14
     * updated  : 2024-05-23
     * param    :
     * return   :
     * explain  : 새 할 일 등록 (할 일 정보, 담당자(nullable), 파일(nullable))
     * */
    public void addTask(AddTaskRequest addTaskRequest) {

        // 1. 새 할 일 정보 등록
        OnTrackTask task = addTaskRequest.dtoToEntityTask();
        taskRepository.newTask(task);

        // 2. history 등록 - ①할 일 명,
        TaskHistory logNewTask = TaskHistory.builder()
                .taskId(task.getId())
                .projectId(task.getProjectId())
                .modItem("title")
                .modType("register")
                .modContent(task.getTaskTitle())
                .updatedAt(task.getCreatedAt())
                .updatedBy(task.getAuthor())
                .build();
        taskRepository.log(logNewTask);
        
        // 3. 담당자 유무 확인
        if (!addTaskRequest.getMemberId().isEmpty()) {

            // 3-1. 담당자 객체(TaskAssignment) 생성 및 DB 저장
            List<TaskAssignment> assignees = addTaskRequest.dtoToEntityTaskAssignment(task.getId());
            taskRepository.assign(assignees);
            
            // 3-2. history 등록 - 담당자 인원만큼 history 객체 생성 및 DB 저장
            IntStream.range(0, assignees.size()).forEach(i -> {
                TaskHistory logAssign = TaskHistory.builder()
                        .taskId(assignees.get(i).getId())
                        .projectId(assignees.get(i).getProjectId())
                        .modItem("assignee")
                        .modType("register")
                        .modContent(assignees.get(i).getNickname()).build();
                taskRepository.log(logAssign);
            });
        }

        // 4. 파일첨부 여부 check 후 객체 생성 및 저장
        //taskRepository.attach();

        

    }

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 수정: 할일명, 중요도, 진행상태, 마감일
     * */

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 수정: 담당자 추가
     * */

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 수정: 담당자 삭제
     * */
    
    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 수정: 파일 추가
     * */

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 수정: 파일 삭제
     * */

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 삭제
     * */

    /*
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 할 일 상세
     * */

    /*
     * created : 2024-05-
     * param   : Long projectId
     * return  :
     * explain : 할 일 목록
     * */
    public List<TasksResponse> findAll(Long projectId){
        return taskRepository.allTasksInProject(projectId);
    }




}
