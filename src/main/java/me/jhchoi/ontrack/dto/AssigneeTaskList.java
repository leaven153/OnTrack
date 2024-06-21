package me.jhchoi.ontrack.dto;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true) // 이 클래스가 java.lang.Object를 확장하지 않은 경우에도 상위 클래스를 호출하지 않고 equals/hashCode 구현을 생성합니다. 의도적인 경우에는 타입에 '(call Super=false)'를 추가하세요.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeTaskList extends TaskList{

    private Long id;
    private String taskTitle;
    private Integer taskStatus;
    private LocalDate taskDueDate;
    private Integer assigneeNum;

}
