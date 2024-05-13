package me.jhchoi.ontrack.mapper;

import me.jhchoi.ontrack.domain.TaskAssignment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskAssignmentMapper {

    // 담당자 배정
    void save(TaskAssignment taskAssignment);
    
    // 담당자 삭제
    void deleteById(TaskAssignment taskAssignment);

}
