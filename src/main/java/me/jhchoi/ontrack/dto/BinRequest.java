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
    private String type; //
    private Long projectId;


    private List<Long> taskIds; // 프로젝트 내에서 task 삭제할 경우
    private List<Map<String, Long>> projectAndTaskId; // 내 일 모아보기에서 삭제/휴지통에서 여러 개를 복원할 경우

    private Long deletedBy;
    private LocalDateTime deletedAt;

    private Long restoredBy;
    private LocalDateTime restoredAt;

}
