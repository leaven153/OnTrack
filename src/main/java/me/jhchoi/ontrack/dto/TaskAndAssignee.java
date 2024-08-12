package me.jhchoi.ontrack.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Long.parseLong;

@Data @Slf4j
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TaskAndAssignee {
    private Long id;
    private String taskTitle;
    private Long authorMid; // OnTrackTask - author
    private String authorName;
    private Integer taskPriority; // vip: 1, ip: 2, norm: 3
    private Integer taskStatus; // 시작 안 함: 1, 계획중: 2, 진행중: 3, 검토중: 4, 완료: 5
    private LocalDate taskDueDate;
    private Long taskParentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long updatedBy;

    // taskForm에서 사용
    private Long projectId;

//    private List<Long> taskIds; // 할 일 삭제 시 사용
//    private Long deletedBy; // 할 일 삭제 시 사용
//    private LocalDateTime deletedAt; // 할 일 삭제 시 사용

    // 할 일의 담당자
    private Integer assigneeNum;
    private String assigneeMid; // DB에서 concat으로 가져오는 값
    private String assigneeName; // DB에서 concat으로 가져오는 값
    private List<Long> assigneeMids;
    private List<String> assigneeNames;
    private Map<Long, String> assignees;

    //  할 일의 파일
    private Integer taskFileCnt;
    private List<MultipartFile> taskFiles;

    public OnTrackTask dtoToEntityTask(){
        LocalDateTime nowWithNano = LocalDateTime.now();
        int nanosec = nowWithNano.getNano();

        return OnTrackTask.builder()
                .projectId(projectId)
                .authorMid(authorMid)
                .authorName(authorName)
                .taskTitle(taskTitle)
                .taskPriority(taskPriority)
                .taskStatus(1)
                .taskDueDate(taskDueDate)
                .createdAt(nowWithNano.minusNanos(nanosec))
                .updatedAt(nowWithNano.minusNanos(nanosec))
                .updatedBy(authorMid)
                .build();
    }

    public List<TaskAssignment> dtoToEntityTaskAssignment(Long taskId, LocalDateTime createdAt){
        List<TaskAssignment> ta = new ArrayList<>();
        IntStream.range(0, assigneeMids.size()).forEach(i -> ta.add(TaskAssignment.builder()
                .projectId(projectId)
                .taskId(taskId)
                .memberId(assigneeMids.get(i))
                .nickname(assigneeNames.get(i))
                .role("assignee")
                .assignedAt(createdAt)
                .build()));
        return ta;
    }

    public void makeAssigneeMap(){
        this.assignees = new HashMap<>();
        if(this.assigneeMid != null){
            if(this.assigneeMid.contains(",")){

                String[] mid = this.assigneeMid.split(",");
                List<String> mids = Arrays.asList(mid);
                this.assigneeMids = mids.stream().map(Long::parseLong).collect(Collectors.toList());
                String[] mName = this.assigneeName.split(",");
                this.assigneeNames = Arrays.asList(mName);
                for(int i = 0; i < mid.length; i++){
                    this.assignees.put(parseLong(mid[i]), mName[i]);
                }
            } else {
                this.assignees.put(parseLong(this.assigneeMid), this.assigneeName);
            }
        } else {
            this.assignees.put(0L, "noassignee");
        }

    }

    // 담당자 여러 명일 때, 성만 출력되도록 한다. (projectView.html에서 직접 호출)
    public static LinkedHashMap<Long, String> manyAssigneeFirstName(Map<Long, String> assignees){
        LinkedHashMap<Long, String> result = new LinkedHashMap<>();
        for(Long key: assignees.keySet()){
            result.put(key, assignees.get(key).substring(0, 1));
        }
        return result;
    }
    
    // 진행상태 한글로 전환 (projectView.html에서 직접 호출)
    public static String switchStatusToKor(int dbStatus){
        String switchedStatus;
        switch (dbStatus){
            case 0 -> switchedStatus = "보류"; // pause = 0
            case 2 -> switchedStatus = "계획중"; // planning = 2
            case 3 -> switchedStatus = "진행중"; // ing = 3
            case 4 -> switchedStatus = "검토중"; // review = 4
            case 5 -> switchedStatus = "완료"; // done = 5
            default -> switchedStatus = "시작 안 함"; // not-yet = 1
        }
        return switchedStatus;
    }

    // 진행상태 영어로 전환 for CSS (projectView.html에서 직접 호출)
    public static String switchStatusToEng(int dbStatus){
        String switchedStatus;
        switch (dbStatus){
            case 0 -> switchedStatus = "pause"; // pause = 0
            case 2 -> switchedStatus = "planning"; // planning = 2
            case 3 -> switchedStatus = "ing"; // ing = 3
            case 4 -> switchedStatus = "review"; // review = 4
            case 5 -> switchedStatus = "done"; // done = 5
            default -> switchedStatus = "not-yet"; // not-yet = 1
        }
        return switchedStatus;
    }

    // 진행상태별 할 일 개수 (projectView.html에서 직접 호출)
    public static Integer countStatus(List<?> taskList, Integer status){
        Integer cnt = 0;
//        log.info("진행상태 별 할 일 개수 함수에 들어온 taskList의 instanceOf가 AssigneeTaskList인가?: {}", taskList instanceof AssigneeTaskList); // 늘 false뿐
//        log.info("countStatus에 들어온 taskList: {}", taskList);
        for (Object list : taskList) {
           if (list instanceof TaskAndAssignee tl) {
               if(Objects.equals(tl.getTaskStatus(), status)) cnt++;
           }
//           else if (list instanceof  AssigneeTaskList atl) {
//               //경고:(62, 23) 조건 'list instanceof AssigneeTaskList atl'은(는) 항상 'false'입니다.
//               if(Objects.equals(atl.getTaskStatus(), status)) cnt++;
//           }
        }
        return cnt;
    }


    // 공동작업 개수, 개인작업 개수 (projectView.html에서 직접 호출)
    public static Integer cntTeamOrSoloTask(List<TaskAndAssignee> tList, String type, Integer status, String assigneName){
        // condition: team > 1, solo == 1
        Integer cnt = 0;
//        log.info("cntTeamOrSoloTask에 넘어온 taskList가 AssigneeTaskList인가: {}", tList instanceof AssigneeTaskList); //false뿐이다..
        for (TaskAndAssignee assigneeTaskList : tList) {
            if (Objects.equals(assigneeTaskList.getTaskStatus(), status) && type.equals("team") && assigneeTaskList.getAssigneeNum() > 1) cnt++;
            if (Objects.equals(assigneeTaskList.getTaskStatus(), status) && type.equals("solo") && assigneeTaskList.getAssigneeNum() == 1) cnt++;
        }
//        log.info("cntTeamOrSolo로 들어온 assinge 이름: {}", assigneName);
//        log.info("cntTeamOrSolo로 들어온 tList: {}", tList);
//        log.info("cntTeamOrSolo로 들어온 type: {}", type);
//        log.info("cntTeamOrSolo로 들어온 status: {}", status);
//        log.info("공동작업/개인작업 개수: {}",cnt);
        return cnt == 0?null:cnt;
    }

    // 멤버 목록 중 해당 task에 이미 배정된 멤버는 제외 (projectView.html에서 직접 호출)
    public static Map<Long, String> unassignedMember(List<MemberInfo> memberInfo, Map<Long, String> assigneeList){
        Map<Long, String> unAssignedMember = new HashMap<>();
        IntStream.range(0, memberInfo.size()).forEach(i -> {
            if(!memberInfo.get(i).getNickname().equals(assigneeList.get(memberInfo.get(i).getMemberId()))){
                unAssignedMember.put(memberInfo.get(i).getMemberId(), memberInfo.get(i).getNickname());
            }
        });
        return unAssignedMember;
    }

    // 해당 task의 수정권한 확인 (projectView.html에서 직접 호출)
    // 작성자이거나 담당자일 때 수정 가능: 할일명, 진행상태, 마감일, 담당자 해제(혹은 이 일에서 빠지기)
    public boolean chkEditAuth(Long mId, TaskAndAssignee task){
        if (Objects.equals(task.getAuthorMid(), mId)) return true;
        Map<Long, String> assignees = task.getAssignees();
        for(Map.Entry<Long, String> entry: assignees.entrySet()){
            if (Objects.equals(entry.getKey(), mId)) return true;
        }
        return false;
    }


    // 해당 task의 담당자인지 확인.. 아직 사용하지않음.
    public boolean chkAssigned(Long mId, TaskAndAssignee task){
        Map<Long, String> assignees = task.getAssignees();
        for(Map.Entry<Long, String> entry: assignees.entrySet()){
            if(Objects.equals(entry.getKey(), mId)) return true;
        }
        return false;
    }

    // projectView.html에서 직접 사용
    public static String[] switchStatusToCss(Integer status){
        String[] css;
        switch(status){
            case 0 -> css = new String[]{"보류", "pause", "pause-bg", "pause-border-shadow"};
            case 2 -> css = new String[]{"계획중", "planning", "planning-bg008", "planning-border-shadow"};
            case 3 -> css = new String[]{"진행중", "ing", "ing-bg008", "ing-border-shadow"};
            case 4 -> css = new String[]{"검토중", "review", "review-bg008", "review-border-shadow"};
            case 5 -> css = new String[]{"완료", "done", "done-bg008", "done-border-shadow"};
            default -> css = new String[]{"시작 안 함", "not-yet", "notYet-bg20", "notYet-border-shadow"};
        }
        return css;
    }
    // 소통하기
    
    // 진행내역
    
    // 파일

}
