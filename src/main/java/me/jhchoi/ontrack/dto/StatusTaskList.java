package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.TaskFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;


@Data @NoArgsConstructor @AllArgsConstructor
public class StatusTaskList extends TaskList{
    private Long id;
    private String taskTitle;
    private Integer taskStatus;
    private Long authorMid;
    private String authorName;
    private LocalDate taskDueDate;
    private String assigneeMid;
    private String assigneeName;
    private Map<Long, String> assignees;

    public void makeAssigneeMap(){
        this.assignees = new HashMap<>();
        if(this.assigneeMid.contains(",")){
            String[] mid = this.assigneeMid.split(",");
            String[] mName = this.assigneeName.split(",");
            for(int i = 0; i < mid.length; i++){
                this.assignees.put(parseLong(mid[i]), mName[i]);
            }
        } else {
            this.assignees.put(parseLong(this.assigneeMid), this.assigneeName);
        }
    }


    // projectView.html에서 직접 사용
    public static String[] switchStatusToCss(Integer status){
        String[] css = new String[]{};
        switch(status){
            case 0 -> css = new String[]{"보류", "pause", "pause-bg", "pause-border-shadow"};
            case 1 -> css = new String[]{"계획중", "planning", "planning-bg008", "planning-border-shadow"};
            case 2 -> css = new String[]{"진행중", "ing", "ing-bg008", "ing-border-shadow"};
            case 3 -> css = new String[]{"검토중", "review", "review-bg008", "review-border-shadow"};
            case 4 -> css = new String[]{"완료", "done", "done-bg008", "done-border-shadow"};
            default -> css = new String[]{"시작 안 함", "not-yet", "notYet-bg20", "notYet-border-shadow"};
        }
        return css;
    }
}

