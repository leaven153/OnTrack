package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDeleteRequest {
    private List<Long> taskIds; // 할 일 삭제 시 사용
    private Long deletedBy; // 할 일 삭제 시 사용
    private LocalDateTime deletedAt; // 할 일 삭제 시 사용
}
