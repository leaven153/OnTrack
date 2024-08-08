package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskBin {
    private Long id;
    private Long projectId;
    private Long taskId;
    private String taskTitle;
    private Long authorMid;
    private String authorName;
    private Integer taskPriority;
    private Integer taskStatus;
    private LocalDate taskDueDate;
    private Long taskParentId;
    private Boolean hasChild;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private Long deletedBy;
    private LocalDateTime deletedAt;

}
