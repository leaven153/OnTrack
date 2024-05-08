package me.jhchoi.ontrack.repository;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    public OnTrackUser save(OnTrackUser user){
        userMapper.save(user);
        return user;
    }
}
