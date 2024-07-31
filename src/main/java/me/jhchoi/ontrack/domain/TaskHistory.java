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

    // entity로 바꾼다면 컬럼에 포함되지 않도록 할 필드
    private String executorName;


    // 할 일 명 등록
    public static TaskHistory logNewTask(OnTrackTask newTask){
        return TaskHistory.builder()
                .projectId(newTask.getProjectId())
                .taskId(newTask.getId())
                .modItem("title")
                .modType("register")
                .modContent(newTask.getTaskTitle())
                .updatedAt(newTask.getCreatedAt())
                .updatedBy(newTask.getAuthorMid())
                .build();
    }

    public static TaskHistory logAssignment(TaskAssignment assignees, Long authorId) {
        return TaskHistory.builder()
                .projectId(assignees.getProjectId())
                .taskId(assignees.getTaskId())
                .modItem("assignee")
                .modType("register")
                .modContent(assignees.getNickname())
                .updatedAt(assignees.getAssignedAt())
                .updatedBy(authorId)
                .build();
    }

    public static TaskHistory logDueDate(OnTrackTask newTask){
        return TaskHistory.builder()
                .projectId(newTask.getProjectId())
                .taskId(newTask.getId())
                .modItem("dueDate")
                .modItem("register")
                .modContent(String.valueOf(newTask.getTaskDueDate()))
                .build();
    }

    /** history map ideation → DB에 한글로 저장하도록 하자!
     * 할일명: register-등록, update-변경
     * 마감일: register-등록, update-변경('마감일 삭제' 포함)
     * 담당자: register-배정, delete-삭제
     * 진행상태: update-변경
     * */
    //        Map<Integer, String[]> statusMap = new LinkedHashMap<>();
//        statusMap.put(0, new String[]{"보류", "pause"});
//        statusMap.put(1, new String[]{"시작 안 함", "not-yet"});
//        statusMap.put(2, new String[]{"계획중", "planning"});
//        statusMap.put(3, new String[]{"진행중", "ing"});
//        statusMap.put(4, new String[]{"검토중", "review"});
//        statusMap.put(5, new String[]{"완료", "done"});



}
