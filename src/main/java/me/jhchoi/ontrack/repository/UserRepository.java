package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.dto.NewUser;
import me.jhchoi.ontrack.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

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

    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 로그인
     * */
    public LoginUser login(String loginId, String loginPw){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        log.info("repository login 실행: id={}, pw={}", loginId, loginPw);
        Optional<OnTrackUser> user = findByEmail(loginId);

        LoginUser loginUser = LoginUser.builder()
                .userId(user.get().getId())
                .loginId(user.get().getUserEmail())
                .loginPw(user.get().getPassword())
                .userName(user.get().getUserName())
                .build();

        log.info("findByLoginId결과: {}", loginUser);
        log.info("result의 비번매칭: {}", encoder.matches(loginPw, loginUser.getLoginPw()));
        if(!encoder.matches(loginPw, loginUser.getLoginPw())) {
            return null;
        }
        return loginUser;
    }

    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 회원조회: email로 찾기
     * */
    public Optional<OnTrackUser> findByEmail(String email){
        return userMapper.findByEmail(email);
    }


    /**
     * created  : 24-07-09
     * param    :
     * return   :
     * explain  : 회원가입절차: 인증코드로 찾기
     * */
    public Optional<NewUser> findByVerificationCode(String vCode){
        Optional<NewUser> nUser = userMapper.findByVerificationCode(vCode);
//        log.info("vCode를 인코딩 할 필요 없는 거 맞나?: {}", nUser);
        // vCode를 인코딩 할 필요 없는 거 맞나?:
        // Optional[NewUser(userEmail=users1@abc.com, password=null,
        // userName=null, verificationCode=null, verified=false)]
        return nUser;
    }


    /**
     * created  : 24-07-10
     * param    :
     * return   :
     * explain  : 회원가입절차: 인증완료한 회원의 verified 컬럼 상태 변경(false → true)
     * */
    public Integer verifyUser(NewUser newUser){
        return userMapper.verifyUser(newUser);
    }
    
    

}
