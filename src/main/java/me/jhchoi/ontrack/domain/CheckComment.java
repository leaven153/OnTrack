package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// task의 소통하기 중 '모두읽기'요청된 comment에 대해
// 해당 task의 담당자들이 글을 확인했는지 여부 저장 및 조회.
@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class CheckComment {
    private Long id;
    private Long commentId; // taskComment id
    private Long memberId; // member id
    private boolean checked; // checker가 해당 '모두 읽기' 요청 글을 확인했는지 여부
}
