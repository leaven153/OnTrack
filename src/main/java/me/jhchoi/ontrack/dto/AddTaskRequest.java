package me.jhchoi.ontrack.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskFile;

import java.time.LocalDate;
import java.util.List;

@Data// Getter, Setter, ToString, EqualsAndHashCode, RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor @Builder
public class AddTaskRequest {

    @NotEmpty @Size(max=20)
    private String taskTitle;
    private String taskPriority;
    private LocalDate taskDueDate;
    private List<Long> assignees; // memberId
    private List<TaskFile> file; //
    
    // 할 일 등록하는 TaskHistory 객체 생성
    
    // assignees 인원만큼 TaskHistory 객체 생성
    

}
