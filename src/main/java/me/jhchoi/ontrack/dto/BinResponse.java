package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class BinResponse {
    private Long projectId;
    private String projectName;
    private Long taskId;
    private Long memberId;
    private String taskTitle;
    private Long deletedBy;
    private String deleterName; // remove를 진행한 사람
    private LocalDateTime deletedAt;

    private Long authorMid;
    // 영구삭제 권한 (작성자에게만 주어진다. 없을 경우, 복원만 가능)
    private Boolean authorized;

}
