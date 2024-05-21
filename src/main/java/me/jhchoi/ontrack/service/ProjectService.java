package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;


    /**
     * created  : 24-05-21
     * param    : OnTrackProject, ProjectMember
     * return   : void
     * explain  : 새 프로젝트 등록, 해당 프로젝트 생성자 member로 등록
     * */
    public void createProject(OnTrackProject newProj, ProjectMember pm){
        log.info("===============project 생성 service 접근===============");
        projectRepository.save(newProj);
        pm.setProjectId(newProj.getId());
        memberRepository.joinProject(pm);
    }

    /**
     * created  : 24-05-21
     * param    : userId
     * return   : List<OnTrackProject>
     * explain  : my page의 project 목록
     * */
    public List<ProjectList> allMyProjects(Long userId){
        log.info("===============project 목록조회 service 접근===============");
        // 1. project member테이블에서 내 userId와 동일한 row에 있는 project(id+)를 가져온다.
        List<ProjectMember> myProjects = memberRepository.findProjectsByUserId(userId);
        log.info("myProjects: {}", myProjects);
        List<ProjectList> test = myProjects.stream().map(p -> projectRepository.allMyProjects(p.getProjectId())).collect(Collectors.toList());
        log.info("test: {}", test);

        List<Long> projectsIds = myProjects.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());
        log.info("projects ids: {}", projectsIds);

        // member id, project id, position(invited여부), joinedAt
        // 2. 프로젝트를 목록에 담는다.
        List<ProjectList> projectList = new ArrayList<>();
        for (Long projectsId : projectsIds) {
            projectList.add(projectRepository.allMyProjects(projectsId));
        }
        log.info("projectList: {}", projectList);

        // project id가 동일한 member의 id와 position
        // project id가 동일한 project의 정보를 project list에 담는다

        // ★ 초대받은 날짜!!! from project_invitation


        return projectList;
    }

}
