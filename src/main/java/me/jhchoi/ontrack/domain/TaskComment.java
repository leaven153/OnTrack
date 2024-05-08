package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskComment {
    private Long id;
    private Long projectId;
    private Long taskId;
    private Long userId;
    private Long memberId;
    private String type;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public TaskComment(Long projectId, Long taskId, Long memberId, Long userId, String type, String comment, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.memberId = memberId;
        this.userId = userId;
        this.type = type;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
