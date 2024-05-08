package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 프로젝트의 공지글을 멤버들이 읽었는지 확인
@Data
@NoArgsConstructor
public class CheckNotice {
    private Long id;
    private Long projectId;
    private Long noticeId; // == task id
    private Long memberId; // or user id?
    private boolean checked;

    @Builder
    public CheckNotice(Long projectId, Long noticeId, Long memberId, boolean checked) {
        this.projectId = projectId;
        this.noticeId = noticeId;
        this.memberId = memberId;
        this.checked = checked;
    }
}
