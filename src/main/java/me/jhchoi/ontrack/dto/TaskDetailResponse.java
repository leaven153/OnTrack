package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.TaskComment;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.domain.TaskHistory;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskDetailResponse{

    private Long projectId;
    private Long taskId;
    private String taskTitle;
    private Long authorMid;
    private String authorName;
    private Map<Long, String> assignees;
//    private Integer assigneeCnt;
    private String tab;
    
    /** OnTrackTask 에 있지만 현재 포함되지 않은 필드
     *     private Integer taskPriority; // vip: 0, ip: 1, norm: 2
     *     private Integer taskStatus; // not-yet: 1, planning: 2, ing: 3, review: 4, done: 5
     *     private LocalDate taskDueDate;
     *     private Long taskParentId;
     *     private LocalDateTime createdAt;
     *     private LocalDateTime updatedAt;
     *     private Long updatedBy;*/

    // comment (소통)
    private List<TaskComment> taskComments;

    // history (내역: 할일명, 진행상태, 배정, 마감일만 가져온다. )
    private List<TaskHistory> taskHistories;

    // File
    private List<TaskFile> taskFiles;

    public static TaskDetailResponse entityToDTO(TaskAndAssignee taskList, Long projectId){
        return TaskDetailResponse.builder()
                .projectId(projectId)
                .taskId(taskList.getId())
                .taskTitle(taskList.getTaskTitle())
                .authorMid(taskList.getAuthorMid())
                .authorName(taskList.getAuthorName())
                .assignees(taskList.getAssignees())
                .build();
    }

    public static void historyCnt(List<TaskHistory> historyList){
        if(!historyList.isEmpty()){
            for(int i = 0; i < historyList.size(); i++){
                historyList.get(i).setCntN(historyList.size()-i);
            }
        }
    } // historyCnt ends



}
