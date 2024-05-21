package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
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

    // 유저가 소속된(멤버) 프로젝트 조회
    public List<ProjectMember> findProjectsByUserId(Long userId) {
        return memberMapper.findProjectsByUserId(userId);
    }


}