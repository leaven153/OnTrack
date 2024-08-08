package me.jhchoi.ontrack.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.dto.MemberInfo;
import me.jhchoi.ontrack.dto.SearchCond;
import me.jhchoi.ontrack.dto.TaskAndAssignee;
import me.jhchoi.ontrack.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
    }

}
