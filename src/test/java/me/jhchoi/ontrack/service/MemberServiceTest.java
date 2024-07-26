package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;


    @Test @DisplayName("해당 이름을 가진 멤버가 프로젝트에 있는지 검색")
    void findByName() {
        //given
        ProjectMember pm = ProjectMember.builder()
                .projectId(9L)
                .nickname("가")
                .build();

        // when
        List<ProjectMember> result = memberRepository.findByName(pm);

        log.info("**이 들어간 이름을 가진 사람: {}", result);
        log.info("해당 멤버가 있고없을 때 차미(size): {}", result.size()); // 없을 때 0
        log.info("해당 멤버가 있고없을 때 차미(isEmpty): {}", result.isEmpty()); // 없을 때 true

        // cond1) 9L, "공"
        // **이 들어간 이름을 가진 사람: [ProjectMember(id=14,
        // projectId=null, userId=null, nickname=공지철,
        // position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null)]

        // cond2) 9L, "가"
        // **이 들어간 이름을 가진 사람: []
        ResponseEntity<?> member = ResponseEntity.ok().body(result);
        log.info("ResponseEntity의 body: {}", member.getBody());
        // ResponseEntity의 body: [ProjectMember(id=14, projectId=null, userId=null, nickname=공지철, position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null)]
        // 여러 명일 때: [ProjectMember(id=28, projectId=null, userId=null, nickname=스칼렛 요한슨, position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null),
        // ProjectMember(id=33, projectId=null, userId=null, nickname=제임스 서버, position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null)]

        List<ProjectMember> pm1 = (List<ProjectMember>) member.getBody();
//        ProjectMember pm2 = (ProjectMember) member.getBody();

        log.info("여러 명 아닐때도? list: {}", pm1);
        // 여러 명일때 list: [ProjectMember(id=28, projectId=null, userId=null, nickname=스칼렛 요한슨, position=null,
        // capacity=null, joinedAt=null, invitedAt=null, invitedAs=null),
        // ProjectMember(id=33, projectId=null, userId=null, nickname=제임스 서버, position=null, capacity=null,
        // joinedAt=null, invitedAt=null, invitedAs=null)]
//        log.info("여러 명인데 list아님: {}", pm2);
        
        // 여러 명 아닐때도? list: [] // 결과값 없을 때
        
        

    }
}