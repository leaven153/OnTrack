package me.jhchoi.ontrack.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskBinRequest {
    private List<Map<String, Long>> projectAndTaskId; // 내 일 모아보기에서 삭제 / 휴지통에서 복원할 경우
    private List<Long> taskIds; // 프로젝트 내에서 여러 task를 삭제할 경우
    private Long deletedBy;
    private LocalDateTime deletedAt;
    private Map<Long, Long> testId;

    private Long projectId;
    private Long taskId;

    private Long restoredBy;
    private LocalDateTime restoredAt;


}
