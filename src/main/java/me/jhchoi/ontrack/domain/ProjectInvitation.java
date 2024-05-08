package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

// 비회원을 team 프로젝트에 초대했을 때
@Data
@NoArgsConstructor
public class ProjectInvitation {
    private Long id;
    private Long projectId;
    private String email;
    private LocalDateTime invitedAt;
    private String invitedAs; // project member의 position

    @Builder
    public ProjectInvitation(Long projectId, String email, LocalDateTime invitedAt, String invitedAs) {
        this.projectId = projectId;
        this.email = email;
        this.invitedAt = invitedAt;
        this.invitedAs = invitedAs;
    }
}
