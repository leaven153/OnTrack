package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final ProjectMapper projectMapper;

    public OnTrackProject save(OnTrackProject project){
        projectMapper.save(project);
        return project;
    }

    public Optional<OnTrackProject> findById(Long projectId) {
        return projectMapper.findById(projectId);
    }
}
