package me.jhchoi.ontrack.domain;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TaskFile {
    private Long id;
    private Long projectId;
    private Long userId;
    private Long memberId;
    private String fileOrigName; // 업로드한 파일의 원래 이름
    private UUID fileNewName; // 파일명 중복방지를 위해 새로운 이름 부여
    private String fileType; // 파일유형...
    private Long fileSize;
    private String filePath;
    private LocalDateTime createdAt;

    @Builder
    public TaskFile(Long projectId, Long userId, Long memberId, String fileOrigName, UUID fileNewName, String fileType, Long fileSize, String filePath, LocalDateTime createdAt) {
        this.projectId = projectId;
        this.userId = userId;
        this.memberId = memberId;
        this.fileOrigName = fileOrigName;
        this.fileNewName = fileNewName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }
}
