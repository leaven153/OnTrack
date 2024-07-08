package me.jhchoi.ontrack.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender mailSender;
    @Test
    void signUp() {
        // given
        String email = "busmoja@naver.com";
        String vCode = UUID.randomUUID().toString();

        // when
        try {
            MimeMessage message = createMail(email, vCode);
            mailSender.send(message);
        } catch(Exception e){
            e.printStackTrace();
        }

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
                <p>아래 링크를 통해 가입을 완료하고<br/>
                On Track의 서비스를 바로 이용하세요 ;D</p>
                <a href='http://localhost:8080/signup/step3?vCode=%s' style='text-decoration: none'>
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