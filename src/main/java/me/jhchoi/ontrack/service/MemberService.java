package me.jhchoi.ontrack.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.MemberInfo;
import me.jhchoi.ontrack.dto.MyTask;
import me.jhchoi.ontrack.dto.SearchCond;
import me.jhchoi.ontrack.dto.TaskAndAssignee;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;

    // 해당 이름을 가진 멤버가 해당 프로젝트에 존재하는지 검색
    public ResponseEntity<List<ProjectMember>> findByName(SearchCond searchCond){
        ProjectMember pm = ProjectMember.builder()
                .projectId(searchCond.getProjectId())
                .nickname(searchCond.getNickname()).build();
        List<ProjectMember> result = memberRepository.findByName(pm);

        if(result.isEmpty()) {
            return ResponseEntity.badRequest().body(result);
        }
        log.info("member service(pm): {}", pm);
        log.info("member service(result): {}", result);
        return ResponseEntity.ok().body(result);
    } // findByName(SearchCond searchCond) 끝


    // 나의 일 모아보기
    public List<MyTask> getAllMyTasks(Long userId){

//        List<MyTask> allMyTasks = taskRepository.getAllMyTasks(userId);


        return taskRepository.getAllMyTasks(userId);
    }

}
