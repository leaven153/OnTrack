package me.jhchoi.ontrack.repository;

import me.jhchoi.ontrack.domain.ProjectMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void joinProject(){

        ProjectMember pMember = ProjectMember.builder()
                .projectId(1L)
                .userId(1L)
                .nickname("Jessica")
                .position("member")
                .capacity("W")
                .joinedAt(LocalDate.now())
                .build();

        memberRepository.joinProject(pMember);

        assertThat(pMember.getId()).isEqualTo(1);

    }

    @Test
    void multiMember(){

    }
}