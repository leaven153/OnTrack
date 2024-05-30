package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class AssignmentList {
    private Long assigneeMid;
    private String assigneeName;
    private Long taskId;
    private String taskTitle;
    private String taskStatus;
}
