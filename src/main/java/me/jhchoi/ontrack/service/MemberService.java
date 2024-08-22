package me.jhchoi.ontrack.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.CheckComment;
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
        return taskRepository.getAllMyTasks(userId);
    }

    // nav에 출력할 알림내용: ①프로젝트 공지
    public void alarmForProjectNotice(Long userId){

    }

    // nav에 출력할 알림내용: ②프로젝트 초대
    public void alarmForProjectInvitation(Long userId){

    }

    // nav에 출력할 알림내용: ③휴지통
    public Boolean alarmForBin(Long userId){
        List<OnTrackTask> myTaskInBin = taskRepository.existsMyTaskInBin(userId);
        return myTaskInBin.isEmpty();
    }

    // nav에 출력할 알림내용: ④중요 소통
    public Boolean alarmForNoticeComment(Long userId){
        List<CheckComment> uncheckedComment = taskRepository.findUnCheckedCommentByUserId(userId);
        return uncheckedComment.isEmpty();
    }



}
