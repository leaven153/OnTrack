package me.jhchoi.ontrack.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 할 일의 변경사항 저장
// 할 일 탭 중 '내역'에 출력
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskHistory {
    private Long id;
    private Long taskId;
    private Long projectId;
    private String modItem; // 변경한 카테고리: 할일명(title), 중요도(priority), 진행상태(status), 마감일(dueDate), 세부할일분화(child), 담당자(assignee)
    private String modType; // 등록(register), 변경(update), 삭제(delete)
    private String modContent; // 변경한 내용
    private LocalDateTime updatedAt;
    private Long updatedBy; // member id

    // entity로 바꾼다면 컬럼에 포함되지 않도록 할 필드1
    private String executorName;

    // entity로 바꾼다면 컬럼에 포함되지 않도록 할 필드2
    private Integer cntN;


    // 할 일 등록
    public static TaskHistory logNewTask(OnTrackTask newTask){
        return TaskHistory.builder()
                .projectId(newTask.getProjectId())
                .taskId(newTask.getId())
                .modItem("할 일")
                .modType("등록")
                .modContent(newTask.getTaskTitle())
                .updatedAt(newTask.getCreatedAt())
                .updatedBy(newTask.getAuthorMid())
                .build();
    }

    // 할 일 등록 시: 담당자 배정 (cf. 할 일 등록 이후 담당자 배정 때는 JS에서 객체생성해서전달)
    public static TaskHistory logAssignment(TaskAssignment assignees, Long authorId) {
        return TaskHistory.builder()
                .projectId(assignees.getProjectId())
                .taskId(assignees.getTaskId())
                .modItem("담당자")
                .modType("배정")
                .modContent(assignees.getNickname())
                .updatedAt(assignees.getAssignedAt())
                .updatedBy(authorId)
                .build();
    }

    // 할 일 등록 시: 마감일 등록 (cf. 할 일 등록 이후 마감일 변경 때는 JS에서 객체생성해서전달)
    public static TaskHistory logDueDate(OnTrackTask newTask){
        return TaskHistory.builder()
                .projectId(newTask.getProjectId())
                .taskId(newTask.getId())
                .modItem("마감일")
                .modType("등록")
                .modContent(String.valueOf(newTask.getTaskDueDate()))
                .updatedAt(newTask.getUpdatedAt())
                .updatedBy(newTask.getAuthorMid())
                .build();
    }

}
