package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginUser;
import me.jhchoi.ontrack.dto.NewUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;


@Mapper
public interface UserMapper {
    
    // 회원 가입: register로 바꿀..?
    void save(OnTrackUser user);

    // 회원 가입 절차: verification code 인증
    Optional<NewUser> findByVerificationCode(String vCode);

    Optional<OnTrackUser> findByEmail(String email);

    // 로그인
    
    // 회원 정보 수정

    // 회원 탈퇴

}
