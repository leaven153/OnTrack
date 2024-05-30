package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeTaskList extends TaskList{

    Long id;
    String taskTitle;
    Integer taskStatus;
    LocalDate taskDueDate;
    Integer assigneeNum;

}
