package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskAssignment {
    private Long id;
    private Long projectId;
    private Long taskId;
    private Long memberId;
    private Long userId;
    private String role;
    private LocalDate assignedAt;

    @Builder
    public TaskAssignment(Long projectId, Long taskId, Long memberId, Long userId, String role, LocalDate assignedAt) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.memberId = memberId;
        this.userId = userId;
        this.role = role;
        this.assignedAt = assignedAt;
    }
}
