package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.MemberNickNames;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.dto.ReqProjectUser;
import me.jhchoi.ontrack.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * param    : project id
     * return   : Optional<OnTrackProject>
     * explain  : my page의 프로젝트 목록 ver.1(프로젝트 목록 조회)
     * */
    public Optional<OnTrackProject> findByProjectId(Long projectId) {
        return projectMapper.findByProjectId(projectId);
    }

    /**
     * created  : 24-05
     * param    : project id
     * return   : List<ProjectList>
     * explain  : my page의 프로젝트 목록 ver.2 (프로젝트 목록 조회)
     * */
    public List<ProjectList> allMyProjects(Long userId){
        return projectMapper.allMyProjects(userId);
    }

    /**
     * created  : 24-05
     * param    : user id
     * return   : List<MemberNicknames>
     * explain  : 프로젝트 멤버의 별명 목록
     * */
    public MemberNickNames getNickName(ReqProjectUser request){
        return projectMapper.getNicknames(request);
    }
}
