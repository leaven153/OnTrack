package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginUser;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    
    // 회원 가입: register로 바꿀 것
    void save(OnTrackUser user);

    LoginUser findByLoginId(String loginId);

    // 로그인
    
    // 회원 정보 수정

    // 회원 탈퇴

}
