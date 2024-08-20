package me.jhchoi.ontrack.domain;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class ProjectMember {
    private Long id;
    private Long projectId; // not null
    private Long userId; // not null

    private String nickname; // 해당 프로젝트에서 사용하는 이름. 10글자 이하. default는 userName값을 넣어둔다.
    private String position; // not null 생성자(PD)/관리자(D)/멤버(W)/탈퇴/강퇴/게스트(solo-shared: R/RC)
    private String capacity; // 읽기만R/읽고댓글RC/할일작성W/타인글삭제D/프로젝트삭제PD/접근불가(탈퇴, 강퇴)
    private LocalDate joinedAt; // 프로젝트에 합류한 날
    private LocalDate invitedAt;
    private String invitedAs;


}
