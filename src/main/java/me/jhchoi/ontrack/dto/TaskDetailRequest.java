package me.jhchoi.ontrack.dto;

import lombok.*;
import me.jhchoi.ontrack.domain.TaskComment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailRequest {

    private Long projectId;
    private Long taskId;

    // task 작성자가 아닌, 소통/파일 작성자의 멤버 정보
    private Long authorMid;
    private String authorName;
    
    // 소통하기 등록
    private Long commentId;
    private String comment;
    private String commentType; // 중요/일반
    private String createdAt;
    private String modifiedAt;

    // 파일 등록
//    private String fileOrigName;
//    private String fileType;
//    private String fileSize;
//    private String uploadedAt;
    private List<MultipartFile> taskFiles;


    public TaskComment toTaskComment(TaskDetailRequest tdr) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(tdr.createdAt, dateFormat);

        return TaskComment.builder()
                .projectId(tdr.projectId)
                .taskId(tdr.taskId)
                .authorMid(tdr.authorMid)
                .authorName(tdr.authorName)
                .type(tdr.commentType)
                .comment(tdr.comment)
                .createdAt(date)
                .build();
    }

    public TaskComment toTaskCommentforEdit(TaskDetailRequest tdr) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(tdr.modifiedAt, dateFormat);

        return TaskComment.builder()
                .id(tdr.commentId)
                .comment(tdr.comment)
                .modifiedAt(date)
                .build();
    }
}
