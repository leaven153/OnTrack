package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackProject;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ProjectMapper {
    void save(OnTrackProject project);

    Optional<OnTrackProject> findById(Long projectId);
}
