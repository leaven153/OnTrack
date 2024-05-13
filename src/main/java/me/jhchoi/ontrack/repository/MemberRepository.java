package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.mapper.MemberMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final MemberMapper memberMapper;

    public void joinProject(ProjectMember pMember){
        memberMapper.joinProject(pMember);
    }

}
