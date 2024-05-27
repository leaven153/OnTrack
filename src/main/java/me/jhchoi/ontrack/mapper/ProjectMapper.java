package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.dto.ResponseInvitation;
import me.jhchoi.ontrack.dto.MemberNickNames;
import me.jhchoi.ontrack.dto.ProjectList;
import me.jhchoi.ontrack.dto.ReqProjectUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectMapper {
    void save(OnTrackProject project);

    OnTrackProject findByProjectId(Long projectId);

    List<ProjectList> allMyProjects(Long userId);

    List<MemberNickNames> getNicknames(ReqProjectUser request);


}
