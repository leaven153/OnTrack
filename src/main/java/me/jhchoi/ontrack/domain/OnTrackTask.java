package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 새 할 일
// 자동: project id, author(user_id), createdAt, updatedAt, updatedBy(user_id)
// 필수입력: task title
//
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class OnTrackTask {
    private Long id;
    private Long projectId;
    private String taskTitle;
    private Long authorMid;
    private String authorName;
    private Integer taskPriority; // vip: 0, ip: 1, norm: 2
    private Integer taskStatus; // not-yet: 1, planning: 2, ing: 3, review: 4, done: 5
    private LocalDate taskDueDate;
    private Boolean hasChild;
    private Long taskParentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    private Long deletedBy;
    private LocalDateTime deletedAt;

}
