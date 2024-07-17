package me.jhchoi.ontrack.dto;

import lombok.*;
import me.jhchoi.ontrack.domain.TaskComment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;

import java.util.List;

@EqualsAndHashCode(callSuper = true) // 경고:(13, 1) 이 클래스가 java.lang.Object를 확장하지 않은 경우에도 상위 클래스를 호출하지 않고 equals/hashCode 구현을 생성합니다. 의도적인 경우에는 타입에 '(call Super=false)'를 추가하세요.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailRequest extends TaskList {

    private Long projectId;
    private Long taskId;
    private Long authorMid;
    private String authorName;
    private String comment;
    private String commentType;
    private String fileName;

}
