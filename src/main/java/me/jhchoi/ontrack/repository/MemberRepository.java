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
        /**
         * user테이블에서 user_name을 갖고온다
         * (프로젝트에 처음 접근할 때 nickname을 설정하도록 유도한다.)
         *
         * */
        memberMapper.joinProject(pMember);
    }
}