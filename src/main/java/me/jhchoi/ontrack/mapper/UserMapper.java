package me.jhchoi.ontrack.mapper;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.OnTrackUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    void save(OnTrackUser user);
}
