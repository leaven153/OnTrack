package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackProject;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ProjectResponse {
    // 1. 해당 프로젝트 정보: 생성자, 프로젝트 타입(솔로/공유된솔로/팀), 프로젝트명, 마감일, 상태(활성화/비활성화/종료/보관), 생성일
    private OnTrackProject project;

    // 2. 프로젝트 멤버 목록: MemberInfo - userId, projectId, memberId, nickname, Map<Long, String> memberInfo()
    private List<MemberInfo> memberList;

    // 3. 할 일 목록:
    // id, project id, 할 일 명, 작성자mId, 작성자닉네임, 중요도, 진행상태, 마감일, 종속id, 생성일, 최종수정일, 최종수정자mId
    // 할 일 별 담당자 목록(mId, nickname)
    // 할 일의 파일들 추가 요망
    private List<TaskList> taskList;

    // 4-1. 담당자별 할 일
    private List<AssignmentList> assignmentList;

    // 4-2. 담당자 없는 할 일 목록
    private List<NoAssigneeTask> noAssigneeTasks;

    // 5. 진행상태별 할 일
    private LinkedHashMap<Integer, List<StatusTaskList>> statusTaskList;



}
