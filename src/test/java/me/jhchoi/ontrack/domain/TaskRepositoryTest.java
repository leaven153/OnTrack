package me.jhchoi.ontrack.domain;

import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
                .author(1L)
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

    @Test
    void assign(){
        List<TaskAssignment> assignees = new ArrayList<>();

        TaskAssignment ta = TaskAssignment.builder()
                .projectId(1L)
                .taskId(1L)
                .userId(1L)
                .memberId(1L)
                .nickname("Jessica")
                .role("assignee")
                .assignedAt(LocalDate.now())
                .build();
        assignees.add(ta);
        taskRepository.assign(assignees);
        assertThat(ta.getId()).isEqualTo(1L);
    } // test: assign

    @Test
    void log(){

    }

}
