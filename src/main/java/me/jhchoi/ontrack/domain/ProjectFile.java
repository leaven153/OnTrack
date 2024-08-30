package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ProjectFile {
    private Long id;
    private Long noticeId;
    private Long projectId;
    private Long uploaderMid;
    private String fileOrigName;
    private String fileNewName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private LocalDateTime loadedAt;
}
