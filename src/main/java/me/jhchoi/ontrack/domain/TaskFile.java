package me.jhchoi.ontrack.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskFile {
    private Long id;
    private Long projectId;
    private Long taskId;
    private Long memberId;
    private String fileOrigName; // 업로드한 파일의 원래 이름
    private String fileNewName; // 파일명 중복방지를 위해 새로운 이름 부여
    private String fileType; // 파일유형...
    private Long fileSize;
    private String filePath;
    private LocalDateTime createdAt;
    private String deletedBy;

    // entity로 바꾼다면 컬럼에 포함되지 않도록 할 필드1
    private String uploaderName;

    // entity로 바꾼다면 컬럼에 포함되지 않도록 할 필드2
    private String formattedFileSize;

}
