package me.jhchoi.ontrack.repository;

import me.jhchoi.ontrack.domain.ProjectMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

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

    @Test @DisplayName("테스트를 위한 더미DB 입력")
    void multiMember(){
        int[] projects = IntStream.range(9, 19).toArray();
        String[] nicknames = new String[]{
                "Adele",
                "어린왕자", "크러쉬", "서머싯 몸",
                "제임스 서버", "스칼렛 요한슨", "제시카 차스테인",
                "톰 하디", "말콤 글래드웰", "헤밍웨이",
                "도리스 레싱", "장 자크 루소", "장폴 사르트르",
                "윤동주", "공지철", "고윤정",
                "송혜교", "안유진", "나탈리 포트만",
                "코난 오브라이언", "젠다야"
        };
        /*
        IntStream.range(45, 66).forEach(i -> {
            ProjectMember pm = ProjectMember.builder()
                    .projectId((long)projects[i-45])
                    .userId((long)i)
                    .nickname(nicknames[i-45])
                    .position("member")
                    .capacity("W")
                    .joinedAt(LocalDate.now())
                    .build();
            memberRepository.joinProject(pm);
        }); */
    }
}