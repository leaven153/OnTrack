package me.jhchoi.ontrack.repository;

import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.NewUser;
import me.jhchoi.ontrack.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
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

    @Test @DisplayName("회원가입테스트")
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

        IntStream.range(0,29).forEach(i -> {
            OnTrackUser user = OnTrackUser.builder()
                    .userEmail("users"+i+"@abc.com")
                    .password(encoder.encode("asdf123"))
                    .userName(names[i])
                    .registeredAt(LocalDate.now()).build();
            userRepository.save(user);

        });

    }

    @Test @DisplayName("verification code로 가입하려는 유저 찾기")
    void findByVerificationCode(){
        //given
        String vCode = "881ac812-8fa4-48af-b9a0-60dafb77a9c5";
        String vCodeNull = "881ac812-8fa4-48af-b9a0-60dafb77a9c";

        // when
        Optional<NewUser> nUser = userRepository.findByVerificationCode(vCode);
        Optional<NewUser> nullUser = userRepository.findByVerificationCode(vCodeNull);

        // then
//        assertThat(nUser.get().getUserEmail()).isEqualTo("users1@abc.com");
        assertThat(nullUser.isEmpty()).isEqualTo(true);
    }

    @Test @DisplayName("인증코드를 통해 들어온 새 회원 처리과정")
    void signUpVerifyLink(){
        // given
        String vCode = "881ac812-8fa4-48af-b9a0-60dafb77a9c5";
        String vCodeNull = "881ac812-8fa4-48af-b9a0-60dafb77a9c";

        // when: 해당 인증코드를 가진 유저가 있는지 확인한다.
        Optional<NewUser> newUser = userRepository.findByVerificationCode(vCode);
        Optional<NewUser> newUserNull = userRepository.findByVerificationCode(vCodeNull);

        log.info("객체확인(newUser): {}", newUser);
        // 객체확인(newUser): Optional[
        // NewUser(userEmail=users1@abc.com,
        // password=$2a$10$5OiektMZnzXs6oei6SRwGewTaWcsm2Rgh9CTb2pxOKmY5ZpEubZGW,
        // userName=메리 포핀스, verificationCode=null, verified=false)]

        log.info("객체확인(newUserNull): {}", newUserNull);

        // then
        // 잘못된 코드로 들어오는 경우: 유저 없음
        if(newUserNull.isEmpty()){
            ResponseEntity<?> resultNull = ResponseEntity.badRequest().body("해당 유저가 없습니다.");
            log.info("해당 코드를 가진 유저가 없는 경우: {}", resultNull);
            // 해당 코드를 가진 유저가 없는 경우: <400 BAD_REQUEST Bad Request,해당 유저가 없습니다.,[]>
        }

        // 이미 인증을 완료한 회원인 경우
        if(newUser.isPresent() && newUser.get().getVerified() == true){
            ResponseEntity<?> result = ResponseEntity.badRequest().body("이미 인증을 완료했습니다.");
            log.info("이미 인증완료인 경우: {}", result);
            log.info("이미 인증완료인 경우: {}", result.getStatusCode());
            // 이미 인증완료인 경우: <400 BAD_REQUEST Bad Request,이미 인증을 완료했습니다.,[]>
            if(result.getStatusCode().is4xxClientError()) {

                // 잘못된 요청입니다. true
                log.info("잘못된 요청입니다. {}", result.getStatusCode().is4xxClientError());
            }
        }

        // 해당 인증코드를 가진 유저가 있고, 인증절차를 완료하지 않은 것이 맞다면
        if(newUser.isPresent() && newUser.get().getVerified() != true){
            Integer verified = userRepository.verifyUser(newUser.get());
            log.info("업데이트 됐는가?: {}", verified);
            if(verified.equals(1)) {
                ResponseEntity<?> result = ResponseEntity.ok().body(newUser.get());
                log.info(" 경우: {}", result.getBody()); // 이미 인증완료인 경우: 400 BAD_REQUEST


                // 경우: 인증에 성공했습니다.
            }

        }


    }
}
