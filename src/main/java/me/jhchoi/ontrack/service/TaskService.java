package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.TaskEditRequest;
import me.jhchoi.ontrack.dto.TaskFormRequest;
import me.jhchoi.ontrack.dto.FileStore;
import me.jhchoi.ontrack.dto.TaskList;
import me.jhchoi.ontrack.repository.ProjectRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final FileStore fileStore;
    /**
     * created  : 2024-05-14
     * updated  : 2024-05-23, 24
     * param    :
     * return   :
     * explain  : 새 할 일 등록 (할 일 정보, 담당자(nullable), 파일(nullable))
     * */
    @Transactional
    public Long addTask(TaskFormRequest taskFormRequest) {

        // 1. 새 할 일 정보 등록
        OnTrackTask task = taskFormRequest.dtoToEntityTask();

        // 1-1. 할 일 등록
        taskRepository.newTask(task);

        // 2. history 등록 - ①할 일 명,
        taskRepository.log(TaskHistory.logNewTask(task));
        
        // 3. 담당자 유무 확인
        if (taskFormRequest.getAssigneesMid() !=null && taskFormRequest.getAssigneesMid().size() > 0) {

//            taskFormRequest.setAssigneeNames(new ArrayList<>());
            // 3-1. 담당자 nickname 가져오기
//            for(int i= 0; i < taskFormRequest.getAssigneesMid().size(); i++){
//                log.info("task service에서 멤버 id: {}", taskFormRequest.getAssigneesMid().get(i));
//                List<MemberInfo> mList = projectRepository.getNickNames(GetMemberNameRequest.builder().projectId(taskFormRequest.getProjectId()).memberId(taskFormRequest.getAssigneesMid().get(i)).build());
//                taskFormRequest.getAssigneeNames().add(mList.get(0).getNickName());
//            }

            // 3-2. 담당자 객체(TaskAssignment) 생성 및 DB 저장
            List<TaskAssignment> assignees = taskFormRequest.dtoToEntityTaskAssignment(task.getId(), task.getCreatedAt());
            taskRepository.assign(assignees);
            
            // 3-3. history 등록 - ② 담당자 인원만큼 history 객체 생성 및 DB 저장
            IntStream.range(0, assignees.size()).forEach(i -> taskRepository.log(TaskHistory.logAssignment(assignees.get(i), task.getAuthorMid())));
        }

        // 4. 파일첨부 여부 check 후 해당 프로젝트/할일 폴더에 저장 및 TaskFile 객체 생성
        // task 생성 후에 task Id를 가지고 file을 저장할 수 있다.

        if (taskFormRequest.getTaskFile() != null && !taskFormRequest.getTaskFile().isEmpty()) {
            try {
                List<TaskFile> fList = fileStore.storeFile(taskFormRequest.getTaskFile(), task.getProjectId(), task.getId(), task.getAuthorMid(), task.getCreatedAt());
                log.info("task service에서 file list: {}", fList);
                if(fList != null && !fList.isEmpty()) taskRepository.attachFile(fList);
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
    @Transactional
    public ResponseEntity<?> editTaskStatus(TaskHistory th, TaskEditRequest ter){
        Map<Integer, String[]> statusMap = new LinkedHashMap<>();
        statusMap.put(0, new String[]{"보류", "pause"});
        statusMap.put(1, new String[]{"시작 안 함", "not-yet"});
        statusMap.put(2, new String[]{"계획중", "planning"});
        statusMap.put(3, new String[]{"진행중", "ing"});
        statusMap.put(4, new String[]{"검토중", "review"});
        statusMap.put(5, new String[]{"완료", "done"});

        taskRepository.log(th);
        Integer result = taskRepository.editTaskStatus(ter);
        if(result == 1) {
            return ResponseEntity.ok().body(statusMap.get(ter.getStatus()));
        } else {
            return ResponseEntity.badRequest().body("진행상태 수정에 오류가 발생했습니다.");
        }
    }

    /*
     * created : 2024-06-18
     * param   : TaskAssignment, TaskHistory
     * return  : void
     * explain : 할 일 수정: 담당자 추가
     * */
    @Transactional
    public ResponseEntity<?> addAssignee(TaskAssignment ta, TaskHistory th){

        // 예외는 어떻게 처리해야 할까?
        // 이미 삭제된 task 혹은 project에 담당자를 배정하려고 하는 경우?
        // assign에는 그닥 오류가 없을 수 있다. 없는 task, project라 해도
        // 이를 test하고 저장하지 않으니까...

        // 해당 일에 이미 배정(참여)된 담당자인지 확인 필요
        Long assigned = taskRepository.chkAssigned(ta);
        if (ta.getTaskId().equals(assigned)) {
            return ResponseEntity.badRequest().body("이미 배정된 담당자입니다.");
        }

        Integer LIMIT_ASSIGN = 6;
        // 담당자가 6명 미만인지 확인한다.
        Integer cntAssignee = taskRepository.cntAssigneeByTaskId(ta.getTaskId());
        if (LIMIT_ASSIGN.equals(cntAssignee)) {
            return ResponseEntity.badRequest().body("담당자는 6명을 초과할 수 없습니다.");
        }

        List<TaskAssignment> taList = new ArrayList<>();
        taList.add(ta);

        taskRepository.assign(taList);
        taskRepository.log(th);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /*
     * created : 2024-07-16
     * param   : TaskAssignment, TaskHistory
     * return  : void
     * explain : 할 일 수정: 담당자 삭제
     * */
    @Transactional
    public int unassign(TaskAssignment ta, TaskHistory th){

        // Transactional을 붙여줬으므로 아래와 같이 하지 않아도 되긴 하다..
        int result = taskRepository.delAssignee(ta);
        if(result == 1) {
            taskRepository.log(th);
        }
        return result;
    }
    
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
