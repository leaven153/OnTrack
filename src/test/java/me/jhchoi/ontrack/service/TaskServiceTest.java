package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
class TaskServiceTest {

    @Autowired
    TaskRepository taskRepository;

    /**
     * test용 더미: project id: 9, user id: 35(공지철), creator's member id:14, project name: By your side
     * member(creator 1, member 4):
     *  공지철(member id: 14, user id: 35, CREATOR)
     *  Adele(member id: 4, user id: 45)
     *  송혜교(member id: 26, user id: 61)
     *  크러쉬(member id: 27, user id: 47)
     *  스칼렛 요한슨(member id: 28, user id: 50)
     *  task title: "매일 두유", "Tigger can do everything", "경복궁 야간개방", "Deep Time"
     * */
    @Test
    @DisplayName("새 할 일 등록: 담당자가 있고 파일 없는 버전")
    void addTaskWithAssigneeNoFile() {

        Long[] memberIds = {14L, 26L}; //4L, 26L, 27L, 28L, 14L
        String[] nicknames = {"공지철", "송혜교"}; // "Adele", "송혜교", "크러쉬", "스칼렛 요한슨", "공지철"
        String[] titles = {"그 벌들은 다 어디로 갔을까", "Tigger can do everything", "경복궁 야간개방", "Deep Time", "2시탈출 컬투쇼", "인생의 베일", "우리 몸 안내서"};
        TaskAndAssignee taskFormRequest = TaskAndAssignee.builder()
                .projectId(9L)
                .authorMid(28L)
                .authorName("스칼렛 요한슨")
                .taskTitle(titles[5])
                .taskPriority(2)
                .assigneeMids(List.of(memberIds))
                .assigneeNames(List.of(nicknames))
                .taskDueDate(LocalDate.now())
                .build();

        OnTrackTask task = taskFormRequest.dtoToEntityTask();

        taskRepository.addTask(task);

        taskRepository.log(TaskHistory.logNewTask(task));

        if (!taskFormRequest.getAssigneeMids().isEmpty()) {

            // 3-1. 담당자 객체(TaskAssignment) 생성 및 DB 저장
            List<TaskAssignment> assignees = taskFormRequest.dtoToEntityTaskAssignment(task.getId(), task.getCreatedAt());
            log.info("assignees 만들어진 상태: {}", assignees);
            taskRepository.assign(assignees);

            // 3-2. history 등록 - ② 담당자 인원만큼 history 객체 생성 및 DB 저장
            IntStream.range(0, assignees.size()).forEach(i -> taskRepository.log(TaskHistory.logAssignment(assignees.get(i), 14L)));
        }
    }

    @Test @DisplayName("할 일의 진행내역(history) 조회")
    void getTaskHistory(){
        //given
        Long taskId = 8L; // task history 있는 일
        Long noHistroryTaskId = 7L;

        //when
        List<TaskHistory> hasTh = taskRepository.getTaskHistory(taskId);
        List<TaskHistory> noTh = taskRepository.getTaskHistory(noHistroryTaskId);

        log.info("히스토리가 있는 할 일의 List: {}", hasTh);
        log.info("히스토리가 있는 할 일의 List isEmpty: {}", hasTh.isEmpty()); // 히스토리가 있는 할 일의 List isEmpty: false
        log.info("히스토리가 있는 할 일의 List size: {}", hasTh.size()); // 히스토리가 있는 할 일의 List size: 53

        log.info("히스토리가 없는 할 일의 List: {}", noTh); // 히스토리가 없는 할 일의 List: []
        log.info("히스토리가 없는 할 일의 List isEmpty: {}", noTh.isEmpty()); // 히스토리가 없는 할 일의 List isEmpty: true
        log.info("히스토리가 없는 할 일의 List size: {}", noTh.size()); // 히스토리가 없는 할 일의 List size: 0
    }

    @Test @DisplayName("할 일의 파일 목록 조회")
    void getTaskFile(){
        // given
        Long taskId = 67L;
        Long nofileCase = 72L;

        // when
        List<TaskFile> result1 = taskRepository.getTaskFile(taskId);
        List<TaskFile> result2 = taskRepository.getTaskFile(nofileCase);

        log.info("파일 있을 때: {}", result1);
        // 파일 있을 때: [TaskFile(id=13, projectId=null, taskId=null, memberId=null, fileOrigName=200자,
        // fileNewName=39d3c88f-05a5-4987-aef1-2966c89f7d9e.txt,
        // fileType=txt, fileSize=null, filePath=null, createdAt=2024-06-06T00:00),
        // TaskFile(id=14, projectId=null, taskId=null, memberId=null, fileOrigName=0416,
        // fileNewName=4e780e43-5305-45f1-ac87-b1f752050248.txt, fileType=txt, fileSize=null,
        // filePath=null, createdAt=2024-06-06T00:00)]
        log.info("파일 있을 때 size: {}", result1.size()); // 파일 있을 때 size: 2
        log.info("파일 있을 때 isEmpty: {}", result1.isEmpty()); // 파일 있을 때 isEmpty: false

        log.info("파일 없을 때: {}", result2); // 파일 없을 때: []
        log.info("파일 없을 때 size: {}", result2.size()); // 파일 없을 때 size: 0
        log.info("파일 없을 때 isEmpty: {}", result2.isEmpty()); // 파일 없을 때 isEmpty: true

        if(!result1.isEmpty()) {
            for(int i = 0; i < result1.size(); i++){
                result1.get(i).setFormattedFileSize(FileStore.fileSizeFormatter(result1.get(i).getFileSize()));
            }
        }

        if(!result2.isEmpty()) {
            for(int i = 0; i < result2.size(); i++){
                result2.get(i).setFormattedFileSize(FileStore.fileSizeFormatter(result2.get(i).getFileSize()));
            }
        }
        log.info("파일 있는 것의 사이즈 정리 후: {}", result1);
        // 파일 있는 것의 사이즈 정리 후: [TaskFile(id=13, projectId=null, taskId=null, memberId=25,
        // fileOrigName=200자, fileNewName=39d3c88f-05a5-4987-aef1-2966c89f7d9e.txt, fileType=txt,
        // fileSize=504, filePath=null, createdAt=2024-06-06T00:00, uploaderName=공유,
        // formattedFileSize=0.5KB),
        // TaskFile(id=14, projectId=null, taskId=null, memberId=25, fileOrigName=0416,
        // fileNewName=4e780e43-5305-45f1-ac87-b1f752050248.txt, fileType=txt, fileSize=997,
        // filePath=null, createdAt=2024-06-06T00:00, uploaderName=공유, formattedFileSize=1.0KB)]
    }

    @Test @DisplayName("파일 업로드 후 얻어지는 결과")
    void attachFile(){
        // given
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanoSec = nowWithNano.getNano();
        LocalDateTime createdAt = nowWithNano.minusNanos(nanoSec);

        // when
        List<TaskFile> taskFiles = new ArrayList<>();
        taskFiles.add(TaskFile.builder()
                .projectId(9L)
                .taskId(9L)
                .memberId(14L)
                .fileOrigName("원래이름")
                .fileNewName("uuid이름")
                .fileType("txt")
                .fileSize(250L)
                .filePath("파일경로")
                .createdAt(createdAt)
                .build());
        taskRepository.attachFile(taskFiles);

        // then
        log.info("파일 저장 후, dto에 fileId 담겼나요?: {}", taskFiles);
        // 파일 저장 후, dto에 fileId 담겼나요?: [TaskFile(id=23, projectId=9, taskId=9, memberId=14,
        // fileOrigName=원래이름, fileNewName=uuid이름, fileType=txt, fileSize=250,
        // filePath=파일경로, createdAt=2024-08-04T19:40:47, uploaderName=null,
        // formattedFileSize=null)]

    }

    @Test @DisplayName("파일 삭제")
    void deleteFile(){
        // given
        Long fileId = 30L;

        // when
        TaskFile file = taskRepository.findFileById(fileId);
        String path = Paths.get(file.getFilePath(), file.getFileNewName()).toString();
        log.info("path로?: {}", path);
//        File deleteFile = new File(file.getFilePath() + file.getFileNewName());
        File removeFile = new File(path);
        log.info("파일 경로 맞게 들어갔는가? remove: {}", removeFile);
        log.info("경로가 맞는가 remove: {}", removeFile.exists());
//        log.info("파일 경로 맞게 들어갔는가? delete: {}", deleteFile);
//        log.info("경로가 맞는가 delete: {}", deleteFile.exists());
        if(removeFile.exists()){
            Boolean result = removeFile.delete();
            log.info("파일 지워졌나요?: {}", result);
        }
    } // deleteFile() 테스트 끝

    @Test @DisplayName("삭제된 할 일을 수정할 때 어떤 에러가 발생하는가")
    void editTaskNotExists(){
        // given
        Long notExistsTaskId = 7L;

        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();


        TaskEditRequest editTaskTitle = TaskEditRequest.builder()
                .taskId(notExistsTaskId)
                .title("존재하지 않는 할일을 수정하려고 할 때 ")
                .updatedAt(nowWithNano.minusNanos(nanosec))
                .updatedBy(14L)
                .build();

        // when
        Integer result = taskRepository.editTaskTitle(editTaskTitle);

        log.info("존재하지 않는 할 일을 수정한다면, result는?: {}", result);
        // 존재하지 않는 할 일을 수정한다면, result는?: 0
    } // editTaskTitle test ends

    @Test @DisplayName("할 일 삭제(휴지통으로 이동)")
    void deleteTask(){

        // given
        List<Long> taskIds = new ArrayList<>();
        taskIds.add(21L);
        taskIds.add(22L);

        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanoSec = nowWithNano.getNano();

        TaskBinRequest deleteRequest = TaskBinRequest.builder()
                .taskIds(taskIds)
                .deletedBy(14L)
                .deletedAt(nowWithNano.minusNanos(nanoSec))
                .build();

        List<OnTrackTask> taskList = new ArrayList<>();

        // when
        // 1. ontrack_task 테이블에서 해당 task 정보를 모두 가져온다. Optional<List<OnTrackTask>> task
        /*
        for(int i = 0; i < deleteRequest.getTaskIds().size(); i++) {
            Optional<OnTrackTask> task = taskRepository.findByTaskId(deleteRequest.getTaskIds().get(i));
            task.ifPresent(t -> {
                t.setDeletedAt(deleteRequest.getDeletedAt());
                t.setDeletedBy(deleteRequest.getDeletedBy());
                taskList.add(t);
            });
            // task.ifPresent(taskList::add);
        }

        // 2. task_bin 테이블에 해당 task 정보를 입력한다.
        for(int i = 0; i < taskList.size(); i++) {
            taskRepository.insertIntoBin(taskList.get(i));
        }

        // 3. ontrack_task 테이블에서 해당 task id를 삭제한다. (deletedRequest List<Long> taskIDs)
        for(int i = 0; i < deleteRequest.getTaskIds().size(); i++) {
            taskRepository.delTask(deleteRequest.getTaskIds().get(i));
        }
        */
    }


}