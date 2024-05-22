package me.jhchoi.ontrack.repository;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class ProjectRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;

    @Test @DisplayName("프로젝트 생성테스트")
    void save(){
        LocalDateTime now = LocalDateTime.now();
        int nanosec = now.getNano();
        OnTrackProject project = OnTrackProject.builder()
                .creator(1L)
                .projectType("team")
                .projectName("sixth project test")  // 같은 아이디가 같은 이름을 가진 프로젝트를 (대량)생성할 수 없도록 해야 하나?
                .projectUrl(UUID.randomUUID().toString())
                .projectStatus("activated")
                .projectDueDate(null)
                .createdAt(now.minusNanos(nanosec))
                .updatedAt(now.minusNanos(nanosec)) //new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .build();
        projectRepository.save(project);
        OnTrackProject findProj = projectRepository.findByProjectId(project.getId()).get();
        assertThat(findProj).isEqualTo(project);
    }

    @Test @DisplayName("프로젝트 10개 생성")
    void multisave(){
        int[] users = IntStream.range(35, 66).toArray();
        String[] names = {"By your side", "Just in time", "Not today", "Pray", "Fix you", 
                "Dreamer", "황금가면", "Shut down", "Soul's Anthem", "연극이 끝나고 난 뒤"};
//        log.info("users: {}", users);
        LocalDateTime now = LocalDateTime.now();
        int nanosec = now.getNano();
        IntStream.range(0, 10).forEach(i -> {
            OnTrackProject project = OnTrackProject.builder()
                    .creator((long) users[i])
                    .projectType("team")
                    .projectStatus("activated")
                    .projectName(names[i])
                    .projectUrl(UUID.randomUUID().toString())
                    .createdAt(now.minusNanos(nanosec))
                    .updatedAt(now.minusNanos(nanosec))
                    .build();
            projectRepository.save(project);
            // project member에 생성자 입력하는 걸 누락시킴...
        });
    }

    @DisplayName("모든 프로젝트 목록 조회 (특정 유저가 아닌 프로젝트 전체")
    @Test
    void allProjects() {
        Long[] projectsIds = {1L, 2L, 3L, 4L, 5L, 6L, 8L};

        List<ProjectList> projectList = new ArrayList<>();

        /*
        * projectList: [ProjectList(projectId=1, projectType=team, projectStatus=activated, creatorId=1, creatorName=Jessica, projectName=first project test, createdAt=2024-05-10, updatedAt=2024-05-10, memberId=null, position=null), null, null, null, null, null, ProjectList(projectId=8, projectType=solo, projectStatus=activated, creatorId=36, creatorName=베토벤, projectName=베토벤의 첫 프로젝트, createdAt=2024-05-21, updatedAt=2024-05-21, memberId=null, position=null)]
        * */
        log.info("projectList: {}", projectList);
//        ProjectList pl = projectRepository.allMyProjects(8L);
//        log.info("allMyporject: {}", pl);
    }
}
