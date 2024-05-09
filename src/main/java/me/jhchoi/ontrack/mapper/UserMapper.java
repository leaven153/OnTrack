package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackUser;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    void save(OnTrackUser user);
}
