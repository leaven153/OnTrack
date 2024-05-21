package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProjectServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Test
    void allMyProjects() {
        Long userId = 36L;
        // 1. project member테이블에서 내 userId와 동일한 row에 있는 project(id+)를 가져온다.
        List<ProjectMember> myProjects = memberRepository.findProjectsByUserId(userId);
        log.info("myProjects: {}", myProjects);
        // myProjects: [ProjectMember(id=2, projectId=8, userId=null, nickname=null, position=creator, capacity=null, joinedAt=2024-05-21)]

        List<ProjectList> test = myProjects.stream()
                .map(p -> projectRepository.allMyProjects(p.getProjectId()))
                .collect(Collectors.toList());
        log.info("test: {}", test);
        // test: [ProjectList(projectId=8, projectType=solo, projectStatus=activated, creatorId=36, creatorName=베토벤, projectName=베토벤의 첫 프로젝트, createdAt=2024-05-21, updatedAt=2024-05-21, memberId=null, position=null)]
        List<Long> projectsIds = myProjects.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());

        log.info("projects ids: {}", projectsIds);
        // projects ids: [8]
        // 2. 프로젝트를 목록에 담는다. ver.1.
//        List<OnTrackProject> projectList = new ArrayList<>();
//        for(int i = 0; i < projectsIds.size(); i++){
//            Optional<OnTrackProject> project = projectRepository.findByProjectId(projectsIds.get(i));
//            projectList.add(project.get());
//        }


        // 2. 프로젝트를 목록에 담는다. ver.2.
        List<ProjectList> projectList = new ArrayList<>();
        for (Long projectsId : projectsIds) {
            projectList.add(projectRepository.allMyProjects(projectsId));
        }
        log.info("projectList: {}", projectList);
        // projectList: [ProjectList(projectId=8, projectType=solo, projectStatus=activated, creatorId=36, creatorName=베토벤, projectName=베토벤의 첫 프로젝트, createdAt=2024-05-21, updatedAt=2024-05-21, memberId=null, position=null)]
    }
}