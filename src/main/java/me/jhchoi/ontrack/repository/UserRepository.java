package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginRequest;
import me.jhchoi.ontrack.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 회원가입(등록)
     * */
    public OnTrackUser save(OnTrackUser user){
        userMapper.save(user);
        return user;
    }

    public LoginRequest login(String loginId, String loginPw){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("repository login 실행: id={}, pw={}", loginId, loginPw);
        LoginRequest result = findByLoginId(loginId);
        log.info("findByLoginId결과: {}", result);
        log.info("result의 비번매칭: {}", encoder.matches(loginPw, result.getLoginPw()));
        if(!encoder.matches(loginPw, result.getLoginPw())) {
            return null;
        }
        return result;
    }

    public LoginRequest findByLoginId(String loginId){
        log.info("repository findByLoginId 실행: id={}", loginId);
        return userMapper.findByLoginId(loginId);
    }

}
