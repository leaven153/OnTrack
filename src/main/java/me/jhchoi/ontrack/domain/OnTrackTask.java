package me.jhchoi.ontrack.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

// 새 할 일
// 자동: project id, author(user_id), createdAt, updatedAt, updatedBy(user_id)
// 필수입력: task title
//
@Data
@NoArgsConstructor
public class OnTrackTask {
    private Long id;
    private Long projectId;
    private String taskTitle;
    private Long author;
    private String taskPriority;
    private String taskStatus;
    private LocalDateTime taskDueDate;
    private Long taskParentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    @Builder
    public OnTrackTask(Long projectId, String taskTitle, Long author, String taskPriority, String taskStatus, LocalDateTime taskDueDate, Long taskParentId, LocalDateTime createdAt, LocalDateTime updatedAt, Long updatedBy) {
        this.projectId = projectId;
        this.taskTitle = taskTitle;
        this.author = author;
        this.taskPriority = taskPriority;
        this.taskStatus = taskStatus;
        this.taskDueDate = taskDueDate;
        this.taskParentId = taskParentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
