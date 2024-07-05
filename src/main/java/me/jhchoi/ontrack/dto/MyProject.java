package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class MyProject {
    // 프로젝트 정보 (from ontrack_project 테이블)
    private Long projectId;
    private String projectType;
    private String projectStatus;
    private Long creatorId; // user id
    private String creatorName;
    private String projectName;
    private LocalDate projectDueDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    // 해당 프로젝트에서의 내 정보 (from project_member 테이블)
//    private Long memberId;
//    private String nickname;
    private String position; // 설정버튼, 초대수락버튼 출력여부 결정(creator/member/invited)
    private LocalDate invitedAt; // position이 invited일 경우, 초대받은 날짜
    private String invitedAs;

}
