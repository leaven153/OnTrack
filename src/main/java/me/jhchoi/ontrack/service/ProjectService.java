package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.MemberNickNames;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.dto.ReqProjectUser;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * updated  : 24-05-22
     * param    : userId
     * return   : List<ProjectList>
     * explain  : my page의 project 목록
     * */
    public List<ProjectList> allMyProjects(Long userId){

        log.info("===============project 목록조회 service 접근===============");
        // 1. 해당 유저가 속한 프로젝트 목록 
        List<ProjectList> projectList =projectRepository.allMyProjects(userId);

        // 2. 해당 프로젝트 생성자(들)의 id 목록
        List<Long> creators = projectList.stream().map(ProjectList::getCreatorId).toList();

        // 3. 생성자 id의 각 idx 가져오기(map으로 생성자의 user id를 key으로 하여 index를 value로 매핑)
        Map<Long, Integer> idxOfCreator = new HashMap<>();
        IntStream.range(0, projectList.size()).forEach(i -> {
            for (Long creator : creators) {
                if (Objects.equals(projectList.get(i).getCreatorId(), creator)) {
                    idxOfCreator.put(creator, i);
                }
            }
        });

        // 4. 생성자가 2개 이상의 프로젝트를 가지고 있고, 각각의 프로젝트에서 다른 nickname을 사용할 수 있으므로
        // 생성자의 user id와 project id가 일치하는 row의 nickname을 받아오기 위해
        // 전용 dto 생성
        List<ReqProjectUser> reqList = new ArrayList<>();
        for (ProjectList list : projectList) {
            ReqProjectUser request = ReqProjectUser.builder()
                    .projectId(list.getProjectId())
                    .userId(list.getCreatorId())
                    .build();
            reqList.add(request);
        }

        // 5. data요청
        List<MemberNickNames> mnn = new ArrayList<>();
        IntStream.range(0,projectList.size()).forEach(i -> mnn.add(projectRepository.getNickName(reqList.get(i))));

        // 6. 생성자의 이름을 project List에 매칭하여 저장
        IntStream.range(0, mnn.size()).forEach(i -> {
            // 생성자 id가 키값(mnn.get(i).getUserId())인 map(idxOfCreator)에서
            // value를 가져오면 그것은 생성자의 이름이 들어갈 위치!
            // 해당 인덱스에 생성자의 이름을 넣는다.
            projectList.get(idxOfCreator.get(mnn.get(i).getUserId())).setCreatorName(mnn.get(i).getNickname());
        });

        return projectList;
    }

}
