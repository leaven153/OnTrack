package me.jhchoi.ontrack.domain;
import me.jhchoi.ontrack.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProjectRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;

    @Test
    void save(){
        OnTrackProject project = OnTrackProject.builder()
                .creator(1L)
                .projectType("team")
                .projectName("second project test")  // 같은 이름을 가진 프로젝트를 생성할 수 없도록 해야 하나?
                .projectUrl(UUID.randomUUID().toString())
                .projectStatus("activated")
                .projectDueDate(null)
                .createdAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .updatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        projectRepository.save(project);
    }

}
