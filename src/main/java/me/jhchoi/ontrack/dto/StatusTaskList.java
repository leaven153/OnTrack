package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class StatusTaskList {
    private Long id;
    private String taskTitle;
    private Integer taskStatus;
    private Long authorMid;
    private String authorName;
    private LocalDate taskDueDate;
    private String assigneeMid;
    private String assigneeName;
    private Map<Long, String> assignees;
}
