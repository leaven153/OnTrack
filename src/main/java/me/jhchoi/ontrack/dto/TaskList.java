package me.jhchoi.ontrack.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data @Slf4j
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskList {
    private Long id;
    private String taskTitle;
    private Long authorMid; // OnTrackTask - author
    private String authorName;
    private Integer taskPriority; // vip: 0, ip: 1, norm: 2
    private Integer taskStatus;
    private LocalDate taskDueDate;
    private Long taskParentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    
    // 할 일의 담당자 목록
    private List<Long> assigneeMids;
    private List<String> assigneeNames;
    
    // 담당자 여러 명일 때, 성만 출력되도록 한다. (projectView.html에서 직접 호출)
    public static List<String> manyAssigneeFirstName(List<String> assigneeNames){
        return assigneeNames.stream().map(assignee ->
            assignee.substring(0, 1)
        ).collect(Collectors.toList());
    }
    
    // 진행상태 한글로 전환 (projectView.html에서 직접 호출)
    public static String switchStatus(int dbStatus){
        String switchedStatus;
        switch (dbStatus){
            case 99 -> switchedStatus = "보류"; // pause = 99
            case 1 -> switchedStatus = "계획중"; // planning = 1
            case 2 -> switchedStatus = "진행중"; // ing = 2
            case 3 -> switchedStatus = "검토중"; // review = 3
            case 4 -> switchedStatus = "완료"; // done = 4
            default -> switchedStatus = "시작 안 함"; // not-yet = 0
        }
        return switchedStatus;
    }

    // 진행상태별 할 일 개수 (projectView.html에서 직접 호출)
    public static Integer countStatus(List<?> taskList, Integer status){
        Integer cnt = 0;
//        log.info("진행상태 별 할 일 개수 함수에 들어온 taskList의 instanceOf가 AssigneeTaskList인가?: {}", taskList instanceof AssigneeTaskList); // 늘 false뿐
        log.info("countStatus에 들어온 taskList: {}", taskList);
        for (Object list : taskList) {
           if (list instanceof TaskList tl) {
               if(Objects.equals(tl.getTaskStatus(), status)) cnt++;
           }
//           else if (list instanceof  AssigneeTaskList atl) {
//               //경고:(62, 23) 조건 'list instanceof AssigneeTaskList atl'은(는) 항상 'false'입니다.
//               if(Objects.equals(atl.getTaskStatus(), status)) cnt++;
//           }
        }
        return cnt == 0?null:cnt;
    }


    // 공동작업 개수, 개인작업 개수
    public static Integer cntTeamOrSoloTask(List<AssigneeTaskList> tList, String type, Integer status, String assigneName){
        // condition: team > 1, solo == 1
        Integer cnt = 0;
//        log.info("cntTeamOrSoloTask에 넘어온 taskList가 AssigneeTaskList인가: {}", tList instanceof AssigneeTaskList); //false뿐이다..
        for (AssigneeTaskList assigneeTaskList : tList) {
            if (Objects.equals(assigneeTaskList.getTaskStatus(), status) && type.equals("team") && assigneeTaskList.getAssigneeNum() > 1) cnt++;
            if (Objects.equals(assigneeTaskList.getTaskStatus(), status) && type.equals("solo") && assigneeTaskList.getAssigneeNum() == 1) cnt++;
        }
        log.info("cntTeamOrSolo로 들어온 assinge 이름: {}", assigneName);
        log.info("cntTeamOrSolo로 들어온 tList: {}", tList);
        log.info("cntTeamOrSolo로 들어온 type: {}", type);
        log.info("cntTeamOrSolo로 들어온 status: {}", status);
        log.info("공동작업/개인작업 개수: {}",cnt);
        return cnt == 0?null:cnt;
    }

    // 소통하기
    
    // 진행내역
    
    // 파일

}
