package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.MemberNickNames;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.dto.ReqProjectUser;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toUnmodifiableMap;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ProjectServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Test @DisplayName("내 프로젝트 목록 조회: 특정 유저 id로, project member에서 먼저 project id 뽑아온 버전")
    void allMyProjects1() {
        Long userId = 36L;

        // 1. project member테이블에서 내 userId와 동일한 row에 있는 project(id+)를 가져온다.
        List<ProjectMember> myProjects = memberRepository.findProjectsByUserId(userId);
        log.info("myProjects: {}", myProjects);
        // myProjects: [ProjectMember(id=2, projectId=8, userId=null, nickname=null, position=creator, capacity=null, joinedAt=2024-05-21)]
        // List 타입이 아닐 때
//        List<ProjectList> test = myProjects.stream()
//                .map(p -> projectRepository.allMyProjects(p.getProjectId()))
//                .collect(Collectors.toList());
//        log.info("test: {}", test);
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
//            projectList.add(projectRepository.allMyProjects(projectsId));
        }
        log.info("projectList: {}", projectList);
        // projectList: [ProjectList(projectId=8, projectType=solo, projectStatus=activated, creatorId=36, creatorName=베토벤, projectName=베토벤의 첫 프로젝트, createdAt=2024-05-21, updatedAt=2024-05-21, memberId=null, position=null)]
    } // allMyProjects() ends
    
    @Test @DisplayName("내 모든 프로젝트: 리스트로 먼저 뽑고, 생성자 별명 따로 뽑아오는 버전")
    void allMyProjects(){
        List<ProjectList> pl = projectRepository.allMyProjects(35L);
        log.info("list로 어떻게 나오나?: {}", pl);
        //list로 어떻게 나오나?:
        // [ProjectList(projectId=9, projectType=team, projectStatus=activated, creatorId=35,
        // creatorName=null, projectName=By your side, projectDueDate=null, createdAt=2024-05-22,
        // updatedAt=2024-05-22, memberId=14, position=creator, invitedAt=null),
        // ProjectList(projectId=16, projectType=team, projectStatus=activated, creatorId=42,
        // creatorName=null, projectName=Shut down, projectDueDate=null, createdAt=2024-05-22,
        // updatedAt=2024-05-22, memberId=25, position=member, invitedAt=null)]

        // 생성자들(혹은 멤버들)의 이름 가져오기

        // 1. 생성자들 id 뽑아오기
        List<Long> creators = pl.stream().map(ProjectList::getCreatorId).collect(Collectors.toList());
        log.info("creators: {}", creators); // creators: [35, 42]

        // 2. 생성자 id와 해당 인덱스를 map으로 뽑아온다. (추후 생성자 '이름' 넣기 쉽도록?)
        Map<Long, Integer> idxOfCreator = new HashMap<>();
//        for(int i = 0; i < pl.size(); i++){
//            for (Long creator : creators) {
//                if (Objects.equals(pl.get(i).getCreatorId(), creator)) {
//                    idxOfCreator.put(creator, i);
//                }
//            }
//        }
        IntStream.range(0, pl.size()).forEach(i -> {
            for (Long creator : creators) {
                if (Objects.equals(pl.get(i).getCreatorId(), creator)) {
                    idxOfCreator.put(creator, i);
                }
            }
        });
        log.info("생성자의 id와 그 id의 위치(index): {}", idxOfCreator);
        // 생성자의 user id와 그 id의 위치(index): {0=35, 1=42} >> {35=0, 42=1} user id: 35 = 공지철, user id: 42 = 크리스 에반스

//        Map<Long, Long> creatorList = pl.stream().collect(toUnmodifiableMap(ProjectList::getProjectId, ProjectList::getCreatorId));
//        log.info("Map이 됐나?: {}", creatorList); // Map이 됐나?: {16=42, 9=35} 프로젝트id=userId(creatorId)

        List<ReqProjectUser> reqList = new ArrayList<>();

        for (ProjectList projectList : pl) {
            ReqProjectUser request = ReqProjectUser.builder()
                    .projectId(projectList.getProjectId())
                    .userId(projectList.getCreatorId())
                    .build();
            reqList.add(request);
        }

        // 3. 생성자의 이름 받아오기
        List<MemberNickNames> mnn = new ArrayList<>();
        IntStream.range(0, pl.size()).forEach(i -> {
            mnn.add(projectRepository.getNickNames(reqList.get(i)).get(0));
        });

        IntStream.range(0, mnn.size()).forEach(i -> {
            // 생성자 id가 키값인 map에서 해당 id의 인덱스를 가져온다.
            // 해당 인덱스에 생성자의 이름을 넣는다.
            pl.get(idxOfCreator.get(mnn.get(i).getUserId())).setCreatorName(mnn.get(i).getNickname());
        });

        log.info("생성자 이름까지 완성된 projectList: {}", pl);
        //[ProjectList(projectId=9, projectType=team, projectStatus=activated, creatorId=35, creatorName=공지철,
        // projectName=By your side, projectDueDate=null, createdAt=2024-05-22, updatedAt=2024-05-22,
        // memberId=14, position=creator, invitedAt=null),
        // ProjectList(projectId=16, projectType=team, projectStatus=activated, creatorId=42, creatorName=크리스 에반스,
        // projectName=Shut down, projectDueDate=null, createdAt=2024-05-22, updatedAt=2024-05-22,
        // memberId=25, position=member, invitedAt=null)]
    }
}