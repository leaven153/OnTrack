package me.jhchoi.ontrack.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskList {
    private Long id;
    private String taskTitle;
    private Long authorMid; // OnTrackTask - author
    private String authorName;
    private String taskPriority;
    private String taskStatus;
    private LocalDate taskDueDate;
    private Long taskParentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    
    // 할 일의 담당자 목록
    private List<Long> assigneeMids;
    private List<String> assigneeNames;


    // 소통하기
    
    // 진행내역
    
    // 파일

}
