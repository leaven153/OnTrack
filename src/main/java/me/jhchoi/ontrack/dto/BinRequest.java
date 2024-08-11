package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinRequest {
    private String type; // restore / remove(휴지통으로 이동) / delete(영구삭제)
    private Long projectId;

    private List<Long> taskIds; // 프로젝트 내에서 task 삭제할 경우
    private List<Map<String, Long>> projectAndTaskId; // 내 일 모아보기에서 삭제휴지통에서 복원할 경우

    private Long deletedBy;
    private LocalDateTime deletedAt;

    private Long restoredBy;
    private LocalDateTime restoredAt;


}
