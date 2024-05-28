package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Long authorMid;
    private String authorName;
    private String taskPriority;
    private String taskStatus;
    private LocalDate taskDueDate;
    private Long taskParentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    @Builder
    public OnTrackTask(Long projectId, String taskTitle, Long authorMid, String authorName, String taskPriority, String taskStatus, LocalDate taskDueDate, Long taskParentId, LocalDateTime createdAt, LocalDateTime updatedAt, Long updatedBy) {
        this.projectId = projectId;
        this.taskTitle = taskTitle;
        this.authorMid = authorMid;
        this.authorName = authorName;
        this.taskPriority = taskPriority;
        this.taskStatus = taskStatus;
        this.taskDueDate = taskDueDate;
        this.taskParentId = taskParentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }


}
