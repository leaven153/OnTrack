package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class NoticeDTO {
    private Long id;
    private Long projectId;
    private Long authorMid;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // request
    private List<MultipartFile> noticeFiles;

    // response
    private Long fileId;
    private Integer noticeFileCnt;
    private String fileOrigName;
    private String fileNewName;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private Integer checkedCnt;
    private Integer unCheckedCnt;

}
