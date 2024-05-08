package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackProject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {
    void save(OnTrackProject project);
}
