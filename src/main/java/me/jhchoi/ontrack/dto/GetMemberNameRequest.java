package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class GetMemberNameRequest {
    private Long projectId;
    private Long taskId;
    private Long userId;
    private Long memberId;
    private String nickname;
}
