package me.jhchoi.ontrack.repository;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Slf4j
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;
//    @Autowired
//    FileStore fileStore;

    @Test
    void newTask(){
        /**
         * test용 더미: project id: 9, user id: 35(공지철), creator's member id:14, project name: By your side
         * member(creator 1, member 5):
         *  공지철(member id: 14, user id: 35, CREATOR)
         *  Adele(member id: 4, user id: 45)
         *  송혜교(member id: 26, user id: 61)
         *  크러쉬(member id: 27, user id: 47)
         *  스칼렛 요한슨(member id: 28, user id: 50)
         * */
        OnTrackTask task = OnTrackTask.builder()
                .projectId(1L)
                .taskTitle("first task test")
                .authorMid(1L)
                .taskPriority(0)
                .taskStatus(0)
                .taskDueDate(null)
                .taskParentId(null)
                .createdAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .updatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .updatedBy(1L)
                .build();
        Long savedTaskId = taskRepository.newTask(task);
        assertThat(task.getId()).isEqualTo(savedTaskId);
    }// test: new task

    @Test @Transactional
    @DisplayName("담당자 여러 명 저장")
    void assign(){
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();

        List<TaskAssignment> assignees = new ArrayList<>();
        Long[] memberIds = {4L, 26L, 27L, 28L};
        String[] nicknames = {"Adele", "송혜교", "크러쉬", "스칼렛 요한슨"};

        IntStream.range(0, memberIds.length).forEach(i -> {
            TaskAssignment taa = TaskAssignment.builder()
                    .projectId(9L)
                    .taskId(5L)
                    .memberId(memberIds[i])
                    .nickname(nicknames[i])
                    .role("assignee")
                    .assignedAt(nowWithNano.minusNanos(nanosec))
                    .build();
            assignees.add(taa);
        });
//        TaskAssignment ta = TaskAssignment.builder()
//                .projectId(1L)
//                .taskId(1L)
//                .userId(1L)
//                .memberId(1L)
//                .nickname("Jessica")
//                .role("assignee")
//                .assignedAt(LocalDateTime.now())
//                .build();
//        assignees.add(ta);
        taskRepository.assign(assignees);
//        assertThat(ta.getId()).isEqualTo(1L);
    } // test: assign

     @Test @DisplayName("담당자별 할 일 목록: parameter List<Long>, return List<AssignmentList>")
     void getAssigneeList(){
        Long memberId = 26L;

//        List<TaskList> assignmentList = taskRepository.getAssginementList(memberId);
//        log.info("담당자별 할 일 목록: {}", assignmentList);
        //담당자별 할 일 목록:
         // [AssignmentList(assigneeMid=26, assigneeName=송혜교, taskId=8, taskTitle=Tigger can do everything, taskStatus=ing),
         // AssignmentList(assigneeMid=26, assigneeName=송혜교, taskId=9, taskTitle=경복궁 야간개방, taskStatus=planning),
         // AssignmentList(assigneeMid=26, assigneeName=송혜교, taskId=13, taskTitle=인생의 베일, taskStatus=done)]
     }

     @Test @DisplayName("진행상태별 할 일 목록: parameter Map<Integer, Long>")
    void getStatusView(){
        //given
         Long projectId = 9L;
         Integer[] statusType = {0, 1, 2, 3, 4, 5};
         ProjectResponse pr = new ProjectResponse();
         LinkedHashMap<Integer, List<StatusTaskList>> m = new LinkedHashMap<>();
         List<StatusTaskList> stl = new ArrayList<>();


         for (Integer integer : statusType) {
             StatusViewRequest svr = new StatusViewRequest(projectId, integer);
             List<StatusTaskList> ttt = taskRepository.getStatusView(svr);
             for(int j = 0; j < ttt.size(); j++){
                 ttt.get(j).makeAssigneeMap();
             }
             m.put(integer, ttt);
         }
         pr.setStatusTaskList(m);
         //         log.info("상태별 할 일 목록: {}", pr.getStatusTaskList());

         LinkedHashMap<Integer, List<StatusTaskList>> test = pr.getStatusTaskList();
         log.info("사이즈는 언제나 0~5이므로 6?: {}", test.size());

         // 보류: 0, 시작안함: 1, 계획중: 2, 진행중: 3, 검토중: 4, 완료: 5
         log.info("LinkedHashMap다루기 연습1: {}", test.get(2)); // 계획중인 task의 list(StatusTaskList)
         // LinkedHashMap다루기 연습1: [StatusTaskList(id=9,
         // taskTitle=경복궁 야간개방, taskStatus=2, authorMid=14, authorName=공지철,
         // taskDueDate=null, assigneeMid=26, assigneeName=송혜교, assignees=null)]
         List<StatusTaskList> tList = test.get(1);
         log.info("StatusTaskList의 함수 사용-get(0): {}", tList.get(0).switchStatusToCss(1));
         log.info("StatusTaskList의 함수 사용-get(0): {}", tList.get(0).switchStatusToCss(1)[1]);
         log.info("StatusTaskList의 함수 사용-get(1): {}", tList.get(1).switchStatusToCss(1));
         log.info("LinkedHashMap다루기 연습2: {}", tList.get(2));
         //LinkedHashMap다루기 연습2: StatusTaskList(id=10,
         // taskTitle=그 벌들은 다 어디로 갔을까, taskStatus=1, authorMid=4, authorName=Adele, taskDueDate=2024-05-31, assigneeMid=27, assigneeName=크러쉬, assignees=null)



//         StatusViewRequest svr2 = new StatusViewRequest(projectId, statusType[1]);
//         List<StatusTaskList> stl2 = taskRepository.getStatusView(svr2);
//         log.info("어떻게 담아야 할까: {}", stl2);
//         StatusTaskList stl = taskRepository.getStatusView();


     }

}
