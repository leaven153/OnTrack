package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ProjectNotice {
    private Long id;
    private Long projectId;
    private Long authorMid;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // entity로 바꾼다면 컬럼에 포함되지 않도록 할 필드1
    private String authorName;

}
