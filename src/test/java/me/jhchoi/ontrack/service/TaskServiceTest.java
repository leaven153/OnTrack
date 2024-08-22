package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.*;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

        BinRequest deleteRequest = BinRequest.builder()
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


    @Test @DisplayName("확인 안 한 중요 소통 조회")
    void getUncheckedNoticeComment(){
        // given
        Long userId = 45L;
        List<CheckComment> uncheckedCommentList = taskRepository.findUnCheckedCommentByUserId(userId);
        log.info("서비스에서의 결과: {}", uncheckedCommentList);
        log.info("uncheckedComment의 size: {}", uncheckedCommentList.size()); // uncheckedComment의 size: 21
        log.info("uncheckedComment의 isEmpty: {}", uncheckedCommentList.isEmpty()); // uncheckedComment의 isEmpty: false

        /*
        * 서비스에서의 결과: [CheckComment(id=2, taskId=22, commentId=24, memberId=4, userId=45, checked=false),
        * CheckComment(id=5, taskId=22, commentId=26, memberId=4, userId=45, checked=false)]*/

    }

    @Test @DisplayName("내가 확인하지 않은 중요 소통 글이 있을 때")
    void alarmForNoticeComment(){
        Long userId = 35L;
        List<CheckComment> uncheckedComment = taskRepository.findUnCheckedCommentByUserId(userId);
        log.info("list: {}", uncheckedComment);
        log.info("list size: {}", uncheckedComment.size()); // list size: 5
        log.info("list isEmpty: {}", uncheckedComment.isEmpty()); // list isEmpty: false
    }

    @Test @DisplayName("내 담당 할 일이 휴지통에 있을 때")
    void alarmForBin(){
        // given
        Long userId = 35L; // 크러쉬 47L, 서머싯 몸 48L
        Map<String, Boolean> testMap = new HashMap<>();

        // when
        List<OnTrackTask> myTaskInBin = taskRepository.existsMyTaskInBin(userId);

        log.info("task list: {}", myTaskInBin); // task list: []
        log.info("task list size: {}", myTaskInBin.size()); // task list size: 0
        log.info("task list isEmpty: {}", myTaskInBin.isEmpty()); // task list isEmpty: true

        testMap.put("bin", myTaskInBin.isEmpty());
        log.info("map: {}", testMap);
        for(String key: testMap.keySet()){
            log.info("key: {}", key); // key: bin
            log.info("value: {}", testMap.get(key)); // value: true
        }

    }

    @Test @DisplayName("확인 안 한 유저 리스트")
    void alarmNoticeComment(){
        // given
        Long commentId = 24L;

        // 1. (내가 작성한) 중요 소통의 task id와 확인하지 않은 유저 list를 가져온다.
        List<CheckComment> unchechkedList = taskRepository.findUncheckedCommentByCommentId(commentId);

        // 2. task id, 유저 id list 형태의 map을 만든다.
        Map<Long, List<Long>> taskAndUserList = new HashMap<>();
        Long taskId = unchechkedList.get(0).getTaskId();
        List<Long> userIdList = new ArrayList<>();

        for (int i = 0; i < unchechkedList.size(); i++) {
            userIdList.add(unchechkedList.get(i).getUserId());
        }

        taskAndUserList.put(taskId, userIdList);
        log.info("task id and user id list 전송: {}", taskAndUserList);
        // {22=[45, 47]}

        // 2-1) 해당 map에서 task id 꺼내올 때.
        log.info("task id: {}", taskAndUserList.keySet()); // task id: [22]
        Long tId = (Long) taskAndUserList.keySet().toArray()[0];
        log.info("set to long: {}", tId); // set to long: 22

        // 2-2) 해당 map에서 user list를 list로 따로 뽑는다.
        List<Long> unchekcdUserList = new ArrayList<>();
        for(List<Long> userList: taskAndUserList.values()){
            unchekcdUserList = userList;
//            log.info("list를 출력: {}", userList); // list를 출력: [45, 47]
//            for(int i = 0; i < userIdList.size(); i++){ // user id: 45
//                log.info("user id: {}", userList.get(i)); // user id: 47
//            }
        }
//        List<Long> userList = Arrays.stream((Long[]) taskAndUserList.values().toArray()).toList();
        // java.lang.ClassCastException: class [Ljava.lang.Object; cannot be cast to class
        // [Ljava.lang.Long; ([Ljava.lang.Object; and [Ljava.lang.Long; are in module java.base of loader 'bootstrap')

        log.info("어떻게 되나: {}", unchekcdUserList); // 어떻게 되나: [45, 47]

        // 3. 현재 로그인 중인 유저들. map
        ConcurrentHashMap<Long, Long> testMap = new ConcurrentHashMap<>();
        testMap.put(45L, 25L);
        testMap.put(47L, 26L);
        testMap.put(49L, 27L);
        testMap.put(50L, 28L);


        // 3-1) 현재 로그인 중인 유저들 중, notice comment에 해당되는 user id에 task id를 보낸다.
        List<Long> loginUsers = new ArrayList<>();
        for(Long targetUser: testMap.keySet()){
            if(unchekcdUserList.stream().anyMatch(Predicate.isEqual(targetUser))){
                String data = tId + "," + targetUser;
                log.info("task id와 targetUser id: {}", data);
            }
//            log.info("현재 로그인한 유저들의 id를 받는다: {}", logs);
//            loginUsers.add(logs);
        }

//        log.info("현재 로그인한 유저들의 id list: {}", loginUsers);
        // 현재 로그인한 유저들의 id list: [49, 50, 45, 47]

//        List<Long> finalUnchekcdUserList = unchekcdUserList;
//        List<Long> result = loginUsers.stream().filter(user-> finalUnchekcdUserList.stream().anyMatch(Predicate.isEqual(user))).toList();

//        log.info("매치되는 유저: {}", result);

        // unchekcdUserList.stream().anyMatch(Predicate.isEqual(user))
    }
}