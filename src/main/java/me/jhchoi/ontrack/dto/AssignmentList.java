package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackTask;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class AssignmentList {
    private Long assigneeMid;
    private String assigneeName;
    private List<AssigneeTaskList> tList;
}
