package me.jhchoi.ontrack.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    
    // 담당자 여러 명일 때, 성만 출력되도록 한다.
    public static List<String> manyAssigneeFirstName(List<String> assigneeNames){
        return assigneeNames.stream().map(assignee ->
            assignee.substring(0, 1)
        ).collect(Collectors.toList());
    }
    
    // 진행상태 한글로 전환
    public static String switchStatus(String dbStatus){
        String switchedStatus;
        System.out.println(dbStatus);
        switch (dbStatus){
            case "planning" -> switchedStatus = "계획중";
            case "ing" -> switchedStatus = "진행중";
            case "review" -> switchedStatus = "검토중";
            case "done" -> switchedStatus = "완료";
            default -> switchedStatus = "시작 안 함";
        }
        return switchedStatus;
    }
    // 소통하기
    
    // 진행내역
    
    // 파일

}
