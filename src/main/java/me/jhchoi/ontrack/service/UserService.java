package me.jhchoi.ontrack.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.dto.NewUser;
import me.jhchoi.ontrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender mailSender; // application.properties에 내용 있어야 오류가 안생긴다!

    public LoginUser login(String loginId, String loginPw){
        log.info("service 진입: id={}, pw={}", loginId, loginPw);
        return userRepository.login(loginId, loginPw);
    }

    /**
     * created  : 24-07-08
     * param    :
     * return   :
     * explain  : 회원가입 1/3 (인증 이메일 발송)
     * */
    public ResponseEntity<?> sendVerificationMail(String email) {

        // 중복된 이메일인지 확인
        if(userRepository.findByEmail(email) != null ) {
            return ResponseEntity.badRequest().body("이미 가입된 이메일입니다.");
        }
        // 메일로 전송할 링크 생성(UUID)
        String vCode = UUID.randomUUID().toString();

        // user 테이블에 저장(email의 @ 앞부분을 이름으로 저장한다)
        String userName = email.substring(0, email.indexOf("@"));
        OnTrackUser user = OnTrackUser.builder()
                .userEmail(email)
                .userName(userName)
                .verified(false)
                .verificationCode(vCode)
                .registeredAt(LocalDate.now()).build();
        userRepository.save(user);

        // 메일 전송
        try {
            MimeMessage message = createMail(email, vCode);
            mailSender.send(message);
        } catch(Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    public MimeMessage createMail(String email, String vCode) throws MessagingException, UnsupportedEncodingException {
        String encodedVCode = URLEncoder.encode(vCode, StandardCharsets.UTF_8);

        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.addTo(email);
        helper.setSubject("[OnTrack] 회원 가입을 위한 인증코드입니다.");
        String mailDisplay = """
                <div style='margin-bottom:0; width: 600px; text-align: right'>
                <span style='font-size:24px;font-color:#533e47; font-weight: 600'>On Track</span>
                </div>
                <div style='border:5px solid #533e47; border-radius:40px; width:600px; background-color:#fff5ec; padding:30px 20px 10px 10px; text-align:center'>
                <h2 style='margin-bottom: 3px'>You're on Track! ;)</h2>
                <h4 style='margin-top: 2px'>Step 2/4</h4>
                <p>아래 링크를 클릭하여 가입을 완료하고<br/>
                On Track의 서비스를 바로 이용하세요 ;D</p>
                <a href='http://localhost:8080/signup/step2?vCode=%s' style='text-decoration: none'>
                <p style='display:inline-block; width:200px; padding:10px; background-color: #533e47;color:#fff5ec;text-align:center; border-radius:5px; font-weight:600'>
                가입완료 하러가기
                </p></a>
                </div>
                """.formatted(encodedVCode);
//        mailDisplay += "";
        helper.setText(mailDisplay, true);
        helper.setFrom("OnTrack");


        return msg;
    } // createMail ends


    /**
     * created  : 24-07-09
     * param    :
     * return   :
     * explain  : 회원가입 2/3 (링크 인증)
     * */
    public Optional<NewUser> signUpVerifyLink(String vCode) {
        return userRepository.findByVerificationCode(vCode);
    }

    /**
     * created  : 24-07-08
     * updated  : 24-07-09
     * param    :
     * return   :
     * explain  : 회원가입 3/3 (비밀번호 설정)
     * */
    public ResponseEntity<?> signupPw(NewUser newUser){
        // 해당 email 유저 찾아서
        return null;
    }
}
