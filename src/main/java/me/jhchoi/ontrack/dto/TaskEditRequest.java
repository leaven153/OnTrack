package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskEditRequest {

    private Long taskId;

    private String title;
    private LocalDateTime dueDate;
    private Integer status;

    private LocalDateTime updatedAt;
    private Long updatedBy;
}
