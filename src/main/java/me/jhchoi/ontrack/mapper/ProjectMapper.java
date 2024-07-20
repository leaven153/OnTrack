package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {
    void save(OnTrackProject project);

    OnTrackProject findByProjectId(Long projectId);

    List<MyProject> allMyProjects(Long userId);

    List<MemberInfo> getMemberInfo(MemberInfo request);

    List<TaskAndAssignee> allTasksInProject(Long projectId);


}
