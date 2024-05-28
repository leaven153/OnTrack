package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectMapper {
    void save(OnTrackProject project);

    OnTrackProject findByProjectId(Long projectId);

    List<ProjectList> allMyProjects(Long userId);

    List<MemberNickNames> getNicknames(ReqProjectUser request);

    List<TaskList> allTasksInProject(Long projectId);


}
