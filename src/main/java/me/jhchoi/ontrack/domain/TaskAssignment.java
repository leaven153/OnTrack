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
    private Long userId;
    private Long memberId;
    private String nickname;
    private String role;
    private LocalDateTime assignedAt;

    @Builder
    public TaskAssignment(Long projectId, Long taskId, Long userId, Long memberId, String nickname, String role, LocalDateTime assignedAt) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.userId = userId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.role = role;
        this.assignedAt = assignedAt;
    }

}
