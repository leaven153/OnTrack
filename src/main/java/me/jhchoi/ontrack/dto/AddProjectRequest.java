package me.jhchoi.ontrack.dto;

import lombok.*;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.ProjectMember;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectRequest {
    // 새 프로젝트 입력 정보
    private String newProjectType;
    private String newProjectName;
    private LocalDate dueDate;

    // 추출 정보 (session)
    private Long creator;

    // 생성 정보: url, 생성일, 최종수정일
    private String url;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // 프로젝트 멤버로 등록
//    private String nickname;
//    private String position;
//    private String capacity;
//    private LocalDate joinedAt;


    public OnTrackProject toProjectEntity() {
        LocalDateTime now = LocalDateTime.now();
        int nanosec = now.getNano();
        return OnTrackProject.builder()
                .creator(creator)
                .projectType(newProjectType)
                .projectName(newProjectName)
                .projectUrl(UUID.randomUUID().toString())
                .projectStatus("activated")
                .projectDueDate(dueDate)
                .createdAt(now.minusNanos(nanosec))
                .updatedAt(now.minusNanos(nanosec))
                .build();
    }

    public static ProjectMember creator(Long userId, String userName){
        return ProjectMember.builder()
                .userId(userId)
                .nickname(userName)
                .position("creator")
                .capacity("PD")
                .joinedAt(LocalDate.now())
                .build();
    }


    public static ProjectMember inviteUser(Long projectId, Long userId, String invitedAs){
        // position을 invitedAs의 값으로 변경
        // joinedAt에 날짜 입력
        return ProjectMember.builder()
                .projectId(projectId)
                .userId(userId)
                .position("invited")
                .invitedAt(LocalDate.now())
                .invitedAs(invitedAs)
                .build();
    }


}
