package me.jhchoi.ontrack.domain;

import me.jhchoi.ontrack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Test
    void save(){
        OnTrackUser user = OnTrackUser.builder()
                .userEmail("user30@abc.com")
                .password(encoder.encode("user1234"))
                .userName("베토벤")
                .registeredAt(LocalDate.now())
                .build();
        userRepository.save(user);

        log.info("비밀번호 일치 = {}", encoder.matches("user1234", user.getPassword()));

    }

    @Test
    void multisave(){
        String[] names = new String[]{"토르", "메리 포핀스", "줄리 앤드류스",
                "블랙위도우", "아이언맨", "크리스 에반스",
                "캡틴 아메리카", "비욘세", "Adele",
                "어린왕자", "크러쉬", "서머싯 몸",
                "제임스 서버", "스칼렛 요한슨", "제시카 차스테인",
                "톰 하디", "말콤 글래드웰", "헤밍웨이",
                "도리스 레싱", "장 자크 루소", "장폴 사르트르",
                "윤동주", "공지철", "고윤정",
                "송혜교", "안유진", "나탈리 포트만",
                "코난 오브라이언", "젠다야", "전길남"};

        IntStream.range(10,29).forEach(i -> {
            OnTrackUser user = OnTrackUser.builder()
                    .userEmail("users"+i+"@abc.com")
                    .password("user1234")
                    .userName(names[i])
                    .registeredAt(LocalDate.now()).build();
            userRepository.save(user);

        });

    }
}
