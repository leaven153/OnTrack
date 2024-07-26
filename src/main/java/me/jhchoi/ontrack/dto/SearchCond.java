package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class SearchCond {
    private Long projectId;
    private Long taskId;
    private Long memberId;
    private Long userId;
    private String nickname;
    private String fileName;
}
