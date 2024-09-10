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
        log.info("===============ENTER member repository ===============");
        log.info("member DATA: {}", pMember);
        memberMapper.joinProject(pMember);
    }

    // 회원의 프로젝트 초대 수락/거절
    public void acceptInvitation(ResponseInvitation newCrew) { memberMapper.acceptInvitation(newCrew); }

    // 해당 별칭을 가진 멤버가 프로젝트 내 존재하는지 검색
    public List<ProjectMember> findByName(ProjectMember pm) { return memberMapper.findByName(pm); }
    
    // 유저의 멤버 정보 조회
    public List<ProjectMember> findByUserId(Long userId) { return memberMapper.findByUserId(userId); }

    // 멤버 아이디로 멤버 정보 조회
    public ProjectMember findByMemberId(Long memberId) { return memberMapper.findByMemberId(memberId); }
}