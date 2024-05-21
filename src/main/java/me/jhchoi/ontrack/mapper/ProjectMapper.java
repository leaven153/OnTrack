package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.ProjectList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectMapper {
    void save(OnTrackProject project);

    Optional<OnTrackProject> findByProjectId(Long projectId);

    ProjectList allMyProjects(Long projectId);
}
