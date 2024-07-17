package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.TaskComment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailResponse {

    // comment (소통)
    private List<TaskComment> taskComments;

    // history (내역: 할일명, 진행상태, 배정, 마감일만 가져온다. )
    private List<TaskHistory> taskHistories;

    // File
    private List<TaskFile> taskFiles;
}
