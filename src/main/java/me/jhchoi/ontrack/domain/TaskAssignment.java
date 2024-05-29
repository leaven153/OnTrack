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
public class TaskAssignment {
    private Long id;
    private Long projectId;
    private Long taskId;
    private Long userId;
    private Long memberId;
    private String nickname;
    private String role;
    private LocalDateTime assignedAt;
}
