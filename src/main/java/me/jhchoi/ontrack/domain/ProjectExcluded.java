package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ProjectExcluded {
    private Long id;
    private Long projectId;
    private String nickname;
    private String email;
    private Long excludedBy;
    private LocalDateTime excludedAt;
    private String reason;

    @Builder
    public ProjectExcluded(Long projectId, String nickname, String email, Long excludedBy, LocalDateTime excludedAt, String reason) {
        this.projectId = projectId;
        this.nickname = nickname;
        this.email = email;
        this.excludedBy = excludedBy;
        this.excludedAt = excludedAt;
        this.reason = reason;
    }
}
