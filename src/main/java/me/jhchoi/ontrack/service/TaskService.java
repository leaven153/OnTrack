package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.TasksResponse;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void addTask(OnTrackTask task, List<TaskAssignment> assignees, TaskHistory taskHistory) {

        taskRepository.newTask(task);

        // 추가요망: 담당자 유무 check
        taskRepository.assign(assignees);

        // 추가요망: 파일첨부 여부 check
        //taskRepository.attatch();

        // history 등록 - 할 일 명, 담당자 (추후 AOP로!)
        taskRepository.log(taskHistory);

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
    public List<TasksResponse> findAll(){
        return taskRepository.findAll();
    }




}
