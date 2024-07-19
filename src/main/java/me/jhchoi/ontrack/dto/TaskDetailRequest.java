package me.jhchoi.ontrack.dto;

import lombok.*;
import me.jhchoi.ontrack.domain.TaskComment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;

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
    private Long authorMid;
    private String authorName;
    private String comment;
    private String commentType; // 모두 확인 요청/일반
    private String createdAt;
    private Long[] assigneesMids;

    private String fileName;


    public TaskComment toTaskComment(TaskDetailRequest tdr) throws ParseException {
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

}
