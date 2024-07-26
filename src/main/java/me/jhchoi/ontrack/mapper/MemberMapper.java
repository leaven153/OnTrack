package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.MemberInfo;
import me.jhchoi.ontrack.dto.ResponseInvitation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 프로젝트 초대
    void invite();
    
    // 프로젝트 합류(team)  cf. 프로젝트 생성(solo, creator)
    void joinProject(ProjectMember pMember);

    // 프로젝트 내 별명 수정
    void updateNickName();

    // 프로젝트 내 포지션 변경(관리자/탈퇴/강퇴)
    void updatePosition();
    
    // 프로젝트 강퇴: project_excluded 저장
    void dropout();

    void acceptInvitation(ResponseInvitation newCrew);

    List<ProjectMember> findByName(ProjectMember pm);
}
