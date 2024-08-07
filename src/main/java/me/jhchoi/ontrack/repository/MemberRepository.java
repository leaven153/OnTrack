package me.jhchoi.ontrack.repository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.MemberInfo;
import me.jhchoi.ontrack.dto.ResponseInvitation;
import me.jhchoi.ontrack.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberMapper memberMapper;

    // 프로젝트 멤버 등록: ①생성, ②초대 수락
    public void joinProject(ProjectMember pMember){
        log.info("===============member repository 접근===============");
        log.info("member 정보: {}", pMember);
        memberMapper.joinProject(pMember);
    }

    // 회원의 프로젝트 초대 수락/거절
    public void acceptInvitation(ResponseInvitation newCrew) { memberMapper.acceptInvitation(newCrew); }

    public List<ProjectMember> findByName(ProjectMember pm) { return memberMapper.findByName(pm); }

}