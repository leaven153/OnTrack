package me.jhchoi.ontrack.repository;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

//        List<TaskAndAssignee> assignmentList = taskRepository.getAssginementList(memberId);
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
         LinkedHashMap<Integer, List<TaskAndAssignee>> m = new LinkedHashMap<>();
         List<TaskAndAssignee> stl = new ArrayList<>();


         for (Integer integer : statusType) {
             TaskAndAssignee svr = TaskAndAssignee.builder().projectId(projectId).taskStatus(integer).build();
             List<TaskAndAssignee> ttt = taskRepository.getStatusView(svr);
             for (TaskAndAssignee statusTaskList : ttt) {
                 statusTaskList.makeAssigneeMap();
             }
             m.put(integer, ttt);
         }
         log.info("완료가 안 떠?: {}", m);
         pr.setStatusTaskList(m);
         //         log.info("상태별 할 일 목록: {}", pr.getStatusTaskList());

         LinkedHashMap<Integer, List<TaskAndAssignee>> test = pr.getStatusTaskList();
         log.info("사이즈는 언제나 0~5이므로 6?: {}", test.size()); // 6 맞음

         // 보류: 0, 시작안함: 1, 계획중: 2, 진행중: 3, 검토중: 4, 완료: 5
         log.info("LinkedHashMap다루기 연습1: {}", test.get(2)); // 계획중인 task의 list(StatusTaskList)
         // LinkedHashMap다루기 연습1: [StatusTaskList(id=9,
         // taskTitle=경복궁 야간개방, taskStatus=2, authorMid=14, authorName=공지철,
         // taskDueDate=null, assigneeMid=26, assigneeName=송혜교, assignees=null)]
         List<TaskAndAssignee> tList = test.get(1);
         log.info("StatusTaskList의 함수 사용-get(0): {}", (Object) TaskAndAssignee.switchStatusToCss(1));
         log.info("StatusTaskList의 함수 사용-get(0): {}", TaskAndAssignee.switchStatusToCss(1)[1]);
         log.info("StatusTaskList의 함수 사용-get(1): {}", (Object) TaskAndAssignee.switchStatusToCss(1));
         log.info("LinkedHashMap다루기 연습2: {}", tList.get(2));
         //LinkedHashMap다루기 연습2: StatusTaskList(id=10,
         // taskTitle=그 벌들은 다 어디로 갔을까, taskStatus=1, authorMid=4, authorName=Adele, taskDueDate=2024-05-31, assigneeMid=27, assigneeName=크러쉬, assignees=null)



//         ProjectRequest svr2 = new ProjectRequest(projectId, statusType[1]);
//         List<StatusTaskList> stl2 = taskRepository.getStatusView(svr2);
//         log.info("어떻게 담아야 할까: {}", stl2);
//         StatusTaskList stl = taskRepository.getStatusView();


     }

//    @Test @DisplayName("담당자 없는 할 일 목록 조회")
//    void getNoAssigneeTask(){
//        Long projectId = 9L;
//        List<NoAssigneeTask> nList = taskRepository.getNoAssigneeTask(projectId);
//        log.info("어떻게 나오는가: {}", nList);
//        /**
//         * 어떻게 나오는가:
//         * [NoAssigneeTask(id=30, taskTitle=그대 내 품에, taskStatus=1, taskDueDate=null, authorMid=14, authorName=공지철, createdAt=2024-06-05),
//         * NoAssigneeTask(id=35, taskTitle=해내자, taskStatus=1, taskDueDate=null, authorMid=14, authorName=공지철, createdAt=2024-06-05)]*/
//    }

    @Test @DisplayName("진행상태 수정")
    void editTaskStatus(){
        // given
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();

        TaskEditRequest ter = TaskEditRequest.builder()
                .taskId(19L)
                .status(2)
                .updatedAt(nowWithNano.minusNanos(nanosec))
                .updatedBy(14L)
                .build();
        TaskHistory th = TaskHistory.builder()
                .projectId(9L)
                .taskId(19L)
                .modItem("status")
                .modType("update")
                .modContent("계획중")
                .updatedBy(14L)
                .updatedAt(nowWithNano.minusNanos(nanosec))
                .build();

        // when
        taskRepository.log(th);
        Integer result = taskRepository.editTaskStatus(ter);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test @DisplayName("담당한 일이 하나도 없는 멤버는 어떻게 반환되는가")
    void findTaskByMemberId(){
        //given
        Long memberId = 31L; // 프로젝트 9의 서머싯 몸은 현재 담당한 일이 0개

        //when
        List<TaskAndAssignee> t = taskRepository.findTaskByMemberId(memberId);

        // then
        log.info("null을 어떻게 처리해야 할까?: {}", t);
        // null을 어떻게 처리해야 할까?: []
        log.info("[]는 size가 어떻게 나오는가?: {}", t.size());
        // []는 size가 어떻게 나오는가?: 0
        log.info("그냥 isEmpty로 하면 되나?: {}", t.isEmpty());
        // 그냥 isEmpty로 하면 되나?: true

    }

    @Test @DisplayName("담당자가 하나도 없는 일은 어떻게 반환되는가")
    void cntAssigneeByTaskId(){
        // given
        Long taskId = 35L; // project 9의 톰하디가 쓴 해내자는 담당자가 0인 상태

        // when
        Integer cntAssignee = taskRepository.cntAssigneeByTaskId(taskId);

        log.info("0 row returned는 어떻게 출력되는가: {}", cntAssignee); // 0 row returned는 어떻게 출력되는가: null
    }

    @Test @DisplayName("해당 할 일에 이미 배정된 담당자인지 확인")
    void chkAssigned(){
        // given
        Long taskId = 8L;
        Long assignedMId = 26L; // 송혜교
        Long notAssignedMember = 31L; // 서머싯몸

        TaskAssignment ta1 = TaskAssignment.builder().taskId(taskId).memberId(notAssignedMember).build();
        TaskAssignment ta2 = TaskAssignment.builder().taskId(taskId).memberId(assignedMId).build();

        // when
        Long noresult = taskRepository.chkAssigned(ta1);
        Long result = taskRepository.chkAssigned(ta2);

        // then
        log.info("해당 할 일에 배정되지 않은 담당자는 어떻게 반환되는가?: {}", noresult);
        // 해당 할 일에 배정되지 않은 담당자는 어떻게 반환되는가?: null

        log.info("해당 할 일에 배정된 담당자는 어떻게 반환되는가?: {}", result);
        // 해당 할 일에 배정된 담당자는 어떻게 반환되는가?: 8

        log.info("result == 1L이 맞는 건가: {}", result == 1L);
        // result == 1L이 맞는 건가: false >> 틀리지 진화야!!! task_id를 Select하라고 했으면서!



    }
}
