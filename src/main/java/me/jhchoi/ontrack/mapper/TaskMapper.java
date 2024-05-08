package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {
    void save(OnTrackTask task);

    void update(@Param("taskId") Long taskId, @Param("updateParam")OnTrackTask task);

    Optional<OnTrackTask> findById(Long taskId);

    List<OnTrackTask> findAll(OnTrackTask task);
}
