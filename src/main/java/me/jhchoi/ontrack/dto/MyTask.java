package me.jhchoi.ontrack.dto;

import lombok.*;
import me.jhchoi.ontrack.domain.TaskComment;
import me.jhchoi.ontrack.domain.TaskFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTask {
    // 해당 일의 member id
    private Long memberId;

    // 내가 담당한/작성한 일의 정보 (priority, hasChild, parentId 제외)
    private Long taskId;
    private Long projectId;
    private String taskTitle;
    private Long authorMid;
    private String authorName;
    private Integer taskStatus; // not-yet: 1, planning: 2, ing: 3, review: 4, done: 5
    private LocalDate taskDueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    // 정렬을 위한 값
    private String orderBy;

    // 할 일의 소통·파일 개수
    private Long taskFileCnt;
    private Long taskCommentCnt;


}
