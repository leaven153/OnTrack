package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.OnTrackTask;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ProjectResponse {
    // 해당 프로젝트 정보: 생성자, 프로젝트 타입(솔로/공유된솔로/팀), 프로젝트명, 마감일, 상태(활성화/비활성화/종료/보관), 생성일
    private OnTrackProject project;

    // MemberNickNames: userId, projectId, memberId, nickname, Map<Long, String> memberList()
    private List<MemberNickNames> memberList;

    // 할 일: id, project id, 할 일 명, 작성자mId, 중요도, 진행상태, 마감일, 종속id, 생성일, 최종수정일, 최종수정자mId
    private List<OnTrackTask> taskList;

}
