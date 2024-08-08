package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data // Getter, Setter, ToString, EqualsAndHashCode, RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ProjectBin {
    private Long id;
    private Long projectId;
    private Long creator;
    private String projectType; // 프로젝트 타입: solo-private, solo-shared, team
    private String projectName; // 프로젝트 명(20자 이내)
    private String projectUrl;
    private String projectStatus; // 프로젝트 상태: 활성, 비활성(휴지통), 종료, 삭제예정(discard), 보관됨
    private LocalDate projectDueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 프로젝트 정보(기준 성립 요망)가 업데이트 된 일자
    private Long deletedBy;
    private LocalDateTime deletedAt;


}
