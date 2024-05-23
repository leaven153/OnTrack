package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.ProjectMember;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProjectList {
    // 프로젝트 정보
    private Long projectId;
    private String projectType;
    private String projectStatus;
    private Long creatorId;
    private String creatorName;
    private String projectName;
    private LocalDate projectDueDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // 해당 프로젝트에서의 내 정보
    private Long memberId;
    private String position; // 설정버튼, 초대수락버튼 출력여부 결정(creator/member/invited)
    private LocalDate invitedAt; // position이 invited일 경우, 초대받은 날짜
    private String invitedAs;

    @Builder
    public ProjectList(Long projectId, String projectType, String projectStatus, Long creatorId, String creatorName, String projectName, LocalDate projectDueDate, LocalDate createdAt, LocalDate updatedAt, Long memberId, String position, LocalDate invitedAt, String invitedAs) {
        this.projectId = projectId;
        this.projectType = projectType;
        this.projectStatus = projectStatus;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.projectName = projectName;
        this.projectDueDate = projectDueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.memberId = memberId;
        this.position = position;
        this.invitedAt = invitedAt;
        this.invitedAs = invitedAs;
    }
}
