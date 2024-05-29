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
    private List<Long> taskId;
    private List<String> taskTitle;
    private List<String> taskStatus;
}
