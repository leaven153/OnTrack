package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberNickNames {
    private Long userId;
    private Long projectId;
    private Long memberId;
    private String nickname;
}
