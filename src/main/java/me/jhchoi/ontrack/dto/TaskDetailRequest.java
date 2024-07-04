package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailRequest {
    private Long projectId;
    private Long taskId;
    private String item;
}
