package me.jhchoi.ontrack.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskFormRequest {

    @NotNull
    private Long projectId;
    @NotNull
    private Long taskAuthorMid; // author의 member id
    private String authorName;
    @NotEmpty @Size(max=20)
    private String taskTitle;
    private Integer taskPriority; // 0매우중요vip, 1중요ip, 2일반norm + 99공지
    private LocalDate taskDueDate;

    // 배정된 담당자
    private List<Long> assigneesMid;
    private List<String> assigneeNames;
    
    // 파일 첨부
    private List<MultipartFile> taskFile;

    public OnTrackTask dtoToEntityTask(){
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();

        return OnTrackTask.builder()
                .projectId(projectId)
                .authorMid(taskAuthorMid)
                .authorName(authorName)
                .taskTitle(taskTitle)
                .taskPriority(taskPriority)
                .taskStatus(0)
                .taskDueDate(taskDueDate)
                .createdAt(nowWithNano.minusNanos(nanosec))
                .updatedAt(nowWithNano.minusNanos(nanosec))
                .updatedBy(taskAuthorMid)
                .build();
    }

    public List<TaskAssignment> dtoToEntityTaskAssignment(Long taskId, LocalDateTime createdAt){
        List<TaskAssignment> ta = new ArrayList<>();
        IntStream.range(0, assigneesMid.size()).forEach(i -> ta.add(TaskAssignment.builder()
                .projectId(projectId)
                .taskId(taskId)
                .memberId(assigneesMid.get(i))
                .nickname(assigneeNames.get(i))
                .role("assignee")
                .assignedAt(createdAt)
                .build()));
        return ta;
    }

}
