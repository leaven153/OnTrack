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
     * created : 2024-05-14
     * param   :
     * return  :
     * explain : 새 할 일 등록 (할 일 정보, 담당자(nullable), 파일(nullable))
     * */
    public void addTask(AddTaskRequest addTaskRequest) {

        OnTrackTask task = addTaskRequest.dtoToEntityTask();
        taskRepository.newTask(task);

        // 담당자 유무 확인하여 담당자 객체(TaskAssignment) 생성 및 DB 저장
        if (!addTaskRequest.getMemberId().isEmpty()) {

            List<TaskAssignment> assignees = new ArrayList<>();
//            IntStream.range(0, addTaskRequest.getMemberId().size()).forEach(i -> {
//                TaskAssignment assignee = TaskAssignment.builder()
//                        .projectId(addTaskRequest.getProjectId())
//                        .taskId(task.getId())
//                        .userId(addTaskRequest.getAssigneesUserId().get(i))
//                        .memberId(addTaskRequest.getMemberId().get(i))
//                        .nickname(addTaskRequest.getNickname().get(i))
//                        .build();
//                assignees.add(assignee);
//            });

            taskRepository.assign(assignees);
            
            // 담당자 인원만큼 history 객체 생성하여 DB에 저장
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

        // 추가요망: 파일첨부 여부 check
        //taskRepository.attach();

        // history 등록 - ①할 일 명, ②담당자 (추후 AOP로!)
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
