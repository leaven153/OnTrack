package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskComment {
    private Long id;
    private Long projectId;
    private Long taskId;
    private Long authorMid;
    private String authorName;
    private String type; // 모두 확인요청, 일반
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
