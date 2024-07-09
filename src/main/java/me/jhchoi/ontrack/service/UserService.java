package me.jhchoi.ontrack.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginUser;
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
     * explain  : 회원가입 (인증 이메일 발송)
     * */
    public ResponseEntity<?> signUp(String email) {

        // 메일로 전송할 링크 생성(UUID)
        String vCode = UUID.randomUUID().toString();

        // user 테이블에 저장
        OnTrackUser user = OnTrackUser.builder()
                .userEmail(email)
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
                <p>Step 2/4</p>
                <p>아래 인증코드를 통해 가입을 완료하고<br/>
                On Track의 서비스를 바로 이용하세요 ;D</p>
                <a href='http://localhost:8080/signup/step2?vCode=%s' style='text-decoration: none'>
                <p style='padding:10px; background-color: #533e47;color:#fff5ec;text-align:center; border-radius:5px; font-weight:600'>
                You're On Track!
                </p></a>
                </div>
                """.formatted(encodedVCode);
//        mailDisplay += "";
        helper.setText(mailDisplay, true);
        helper.setFrom("OnTrack");


        return msg;
    }
}
