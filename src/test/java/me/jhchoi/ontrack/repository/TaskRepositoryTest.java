package me.jhchoi.ontrack.repository;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.dto.AssignmentList;
import me.jhchoi.ontrack.dto.TaskList;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @Slf4j
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

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
                .taskPriority("VIP")
                .taskStatus("not yet")
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

}
