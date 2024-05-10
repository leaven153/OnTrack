package me.jhchoi.ontrack.domain;

import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Test
    void save(){
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
        OnTrackTask savedTask = taskRepository.save(task);
        OnTrackTask findTask = taskRepository.findById(task.getId()).get();
        assertThat(findTask.getTaskTitle()).isEqualTo(savedTask.getTaskTitle());
    }
}
