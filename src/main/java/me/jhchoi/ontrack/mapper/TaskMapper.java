package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.OnTrackTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {
    
    // 새 할 일 저장
    void save(OnTrackTask task);

    // 할 일 수정
    void update(@Param("taskId") Long taskId, @Param("updateParam")OnTrackTask task);

    // 할 일 상세
    Optional<OnTrackTask> findById(Long taskId);

    // 할 일 - 탭: 소통하기

    // 할 일 - 탭: 진행내역
    
    // 할 일 - 탭: 파일
    
    // 할 일 목록
    List<OnTrackTask> findAll(OnTrackTask task);
}
