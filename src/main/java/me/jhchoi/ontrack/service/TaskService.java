package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.AddTaskRequest;
import me.jhchoi.ontrack.dto.FileStore;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final FileStore fileStore;
    /**
     * created  : 2024-05-14
     * updated  : 2024-05-23, 24
     * param    :
     * return   :
     * explain  : 새 할 일 등록 (할 일 정보, 담당자(nullable), 파일(nullable))
     * */
    public Long addTask(AddTaskRequest addTaskRequest) {

        // 1. 새 할 일 정보 등록
        OnTrackTask task = addTaskRequest.dtoToEntityTask();

        // 1-1. 할 일 등록
        taskRepository.newTask(task);

        // 2. history 등록 - ①할 일 명,
        taskRepository.log(TaskHistory.logNewTask(task));
        
        // 3. 담당자 유무 확인
        if (addTaskRequest.getAssigneesMid().length > 0) {

            // 3-1. 담당자 객체(TaskAssignment) 생성 및 DB 저장
            List<TaskAssignment> assignees = addTaskRequest.dtoToEntityTaskAssignment(task.getId(), task.getCreatedAt());
            taskRepository.assign(assignees);
            
            // 3-2. history 등록 - ② 담당자 인원만큼 history 객체 생성 및 DB 저장
            IntStream.range(0, assignees.size()).forEach(i -> taskRepository.log(TaskHistory.logAssignment(assignees.get(i), task.getAuthorMid())));
        }

        // 4. 파일첨부 여부 check 후 해당 프로젝트/할일 폴더에 저장 및 TaskFile 객체 생성
        // task 생성 후에 task Id를 가지고 file을 저장할 수 있다.
        if (addTaskRequest.getTaskFile() != null) {
            try {
                List<TaskFile> fList = fileStore.storeFile(addTaskRequest.getTaskFile(), task.getProjectId(), task.getId(), task.getAuthorMid(), task.getCreatedAt());
                taskRepository.attachFile(fList);
            } catch (IOException e) {
                log.info("파일 저장 에러: {}", e.getMessage());
                // 파일 저장 에러: java.io.FileNotFoundException:
                /**
                 * C:\Users/user\AppData\Local\Temp\tomcat.8080.2139694289704046896\work\Tomcat\localhost\ROOT\9\14\9bba9073-0957-4dd9-acb4-0e3103105f27.txt (지정된 경로를 찾을 수 없습니다)
                 * */
            }
        }
        return task.getId();
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


}
