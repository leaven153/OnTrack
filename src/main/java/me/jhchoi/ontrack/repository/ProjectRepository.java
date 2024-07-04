package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final ProjectMapper projectMapper;

    /**
     * created  : 24-05
     * param    : OnTrackProject
     * return   : 
     * explain  : 새 프로젝트 저장/등록
     * */
    public void save(OnTrackProject project){
        log.info("===============project repository 접근===============");
        log.info("인자로 넘어온 project: {}", project);
        projectMapper.save(project);

    }

    /**
     * created  : 24-05
     * param    : Long project id
     * return   : Optional<OnTrackProject>
     * explain  : 프로젝트 정보: id, CREATOR, project_name, project_type, project_status, project_url, project_dueDate, createdAt, updatedAt
     * */
    public OnTrackProject findByProjectId(Long projectId) {
        return projectMapper.findByProjectId(projectId);
    }

    /**
     * created  : 24-05
     * param    : project id
     * return   : List<MyProject>
     * explain  : my page의 프로젝트 목록 ver.2 (프로젝트 목록 조회)
     * */
    public List<MyProject> allMyProjects(Long userId){
        return projectMapper.allMyProjects(userId);
    }

    /**
     * created  : 24-05-25
     * param    : Long projectId
     * return   :
     * explain  : 새 프로젝트 저장/등록
     * */

    /**
     * created  : 24-05-22
     * updated  : 24-05-30
     * param    : GetMemberRequest (projectId, userId(nullable), memberId(nullable))
     * return   : List<MemberInfo>
     * explain  : 프로젝트 멤버 목록
     * */
    public List<MemberInfo> getMemberList(GetMemberNameRequest request){
        return projectMapper.getMemberList(request);
    }

    /**
     * created  : 24-05-28
     * param    : Long project id
     * return   : List<OnTrackTask>
     * explain  : 프로젝트 내 할 일 목록
     * */
    public List<TaskList> allTasksInProject(Long projectId){ return projectMapper.allTasksInProject(projectId); }
    
    /**
     * created  : 24-05-23
     * param    : Long projectId, Long userId
     * return   : void
     * explain  : 회원의 프로젝트 초대 수락
     * */

}
