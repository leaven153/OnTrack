package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.domain.TaskStatusCss;
import me.jhchoi.ontrack.dto.MyTask;
import me.jhchoi.ontrack.dto.TaskAndAssignee;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TaskRepository taskRepository;


    @Test @DisplayName("해당 이름을 가진 멤버가 프로젝트에 있는지 검색")
    void findByName() {
        //given
        ProjectMember pm = ProjectMember.builder()
                .projectId(9L)
                .nickname("가")
                .build();

        // when
        List<ProjectMember> result = memberRepository.findByName(pm);

        log.info("**이 들어간 이름을 가진 사람: {}", result);
        log.info("해당 멤버가 있고없을 때 차미(size): {}", result.size()); // 없을 때 0
        log.info("해당 멤버가 있고없을 때 차미(isEmpty): {}", result.isEmpty()); // 없을 때 true

        // cond1) 9L, "공"
        // **이 들어간 이름을 가진 사람: [ProjectMember(id=14,
        // projectId=null, userId=null, nickname=공지철,
        // position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null)]

        // cond2) 9L, "가"
        // **이 들어간 이름을 가진 사람: []
        ResponseEntity<?> member = ResponseEntity.ok().body(result);
        log.info("ResponseEntity의 body: {}", member.getBody());
        // ResponseEntity의 body: [ProjectMember(id=14, projectId=null, userId=null, nickname=공지철, position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null)]
        // 여러 명일 때: [ProjectMember(id=28, projectId=null, userId=null, nickname=스칼렛 요한슨, position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null),
        // ProjectMember(id=33, projectId=null, userId=null, nickname=제임스 서버, position=null, capacity=null, joinedAt=null, invitedAt=null, invitedAs=null)]

        List<ProjectMember> pm1 = (List<ProjectMember>) member.getBody();
//        ProjectMember pm2 = (ProjectMember) member.getBody();

        log.info("여러 명 아닐때도? list: {}", pm1);
        // 여러 명일때 list: [ProjectMember(id=28, projectId=null, userId=null, nickname=스칼렛 요한슨, position=null,
        // capacity=null, joinedAt=null, invitedAt=null, invitedAs=null),
        // ProjectMember(id=33, projectId=null, userId=null, nickname=제임스 서버, position=null, capacity=null,
        // joinedAt=null, invitedAt=null, invitedAs=null)]
//        log.info("여러 명인데 list아님: {}", pm2);
        
        // 여러 명 아닐때도? list: [] // 결과값 없을 때

    }

    @Test @DisplayName("내 일 모아보기")
    void getAllMyTasks(){
        // given
        Long userId = 47L; // userId 35: 공지철14, 공유25

        // when

        // 0. user id로 내 할 일 정보 가져온다.
        List<MyTask> allMyTasks = taskRepository.getAllMyTasks(userId);
        log.info("그냥 한 번에?: {}", allMyTasks);
        // 그냥 한 번에?: [MyTask(memberId=6, taskId=82, projectId=11, taskTitle=8월 18일은 감사예배, authorMid=null, authorName=null, taskStatus=2, taskDueDate=2024-08-16, createdAt=2024-08-07T12:35:18, updatedAt=2024-08-07T15:32:29, updatedBy=null, orderBy=null, fileCnt=1, commCnt=null)
        // , MyTask(memberId=27, taskId=64, projectId=9, taskTitle=크러쉬와 송혜교만, authorMid=null, authorName=null, taskStatus=1, taskDueDate=null, createdAt=2024-06-06T16:25:46, updatedAt=2024-07-30T23:58:22, updatedBy=null, orderBy=null, fileCnt=null, commCnt=null)
        // , MyTask(memberId=27, taskId=22, projectId=9, taskTitle=Before Sunset, authorMid=null, authorName=null, taskStatus=2, taskDueDate=2024-06-14, createdAt=2024-06-05T18:18:05, updatedAt=2024-07-04T20:30, updatedBy=null, orderBy=null, fileCnt=1, commCnt=3)
        // , MyTask(memberId=27, taskId=27, projectId=9, taskTitle=달과 6펜스, authorMid=null, authorName=null, taskStatus=2, taskDueDate=2024-06-05, createdAt=2024-06-05T21:37:24, updatedAt=2024-07-04T20:25:01, updatedBy=null, orderBy=null, fileCnt=null, commCnt=null)
        // , MyTask(memberId=27, taskId=19, projectId=9, taskTitle=Dreamer, authorMid=null, authorName=null, taskStatus=2, taskDueDate=2024-06-13, createdAt=2024-06-05T17:58:58, updatedAt=2024-07-04T20:10:22, updatedBy=null, orderBy=null, fileCnt=1, commCnt=null)]


        MyTask task = new MyTask();
        task = allMyTasks.get(0);
        MyTask.statusCode(1);
        TaskAndAssignee testT = null;
//        testT.chkEditAuth();
        // css 가져오기 test
        log.info("진행상태 enum: {}", MyTask.statusCode(task.getTaskStatus()));
        // 진행상태 enum: [계획중, planning, planning-bg008, planning-border-shadow]
//        log.info("css: {}", TaskStatusCss.getCss(allMyTasks.get(0).getTaskStatus()).get(0));
        // css: 계획중


        // 1. user id로 project_member의 id list 가져온다.
//        List<ProjectMember> myMemberIdList = memberRepository.findByUserId(userId);
//
//        // 2-1. member id로 ontrack_task에서 task list 가져와서 MyTask list로 만든다.
//        List<OnTrackTask> taskList;
//        Map<Long, List<OnTrackTask>> memberIdandTasks = new HashMap<>();
//        for(int i = 0; i < myMemberIdList.size(); i++) {
//            taskList = taskRepository.findTaskByMemberId(myMemberIdList.get(i).getId());
//            memberIdandTasks.put(myMemberIdList.get(i).getId(), taskList);
//        }
//        log.info("OnTrackTask 리스트: {}", taskList);
        // OnTrackTask 리스트: [OnTrackTask(id=38, projectId=16, taskTitle=게임이 아냐 진 적이 없으니까, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T10:49:40, updatedAt=2024-06-06T10:49:40, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=40, projectId=16, taskTitle=인사이드 아웃2, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=null, hasChild=null, taskParentId=null, createdAt=2024-06-06T10:54:55, updatedAt=2024-06-06T10:54:55, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=42, projectId=16, taskTitle=왜 바로 추가되지 않을까?, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-14, hasChild=null, taskParentId=null, createdAt=2024-06-06T11:01:15, updatedAt=2024-06-06T11:01:15, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=43, projectId=16, taskTitle=js말고 바로 컨트롤러?, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T11:14:42, updatedAt=2024-06-06T11:14:42, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=57, projectId=16, taskTitle=톰과 크리스만, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T13:28:21, updatedAt=2024-06-06T13:28:21, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=60, projectId=16, taskTitle=공유만 선택, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-08, hasChild=null, taskParentId=null, createdAt=2024-06-06T13:40:35, updatedAt=2024-06-06T13:40:35, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=62, projectId=16, taskTitle=파일첨부안함, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T13:46:47, updatedAt=2024-06-06T13:46:47, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=65, projectId=16, taskTitle=무빙, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T16:45:20, updatedAt=2024-06-06T16:45:20, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=66, projectId=16, taskTitle=redirectView와 fetch , authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-08, hasChild=null, taskParentId=null, createdAt=2024-06-06T16:48:35, updatedAt=2024-06-06T16:48:35, updatedBy=null, deletedBy=null, deletedAt=null)
        // , OnTrackTask(id=67, projectId=16, taskTitle=setTimeOut, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-08, hasChild=null, taskParentId=null, createdAt=2024-06-06T16:54:12, updatedAt=2024-06-06T16:54:12, updatedBy=null, deletedBy=null, deletedAt=null)]

//        log.info("member id와 task 리스트 map: {}", memberIdandTasks);
//  member id와 task 리스트 map: {25=[OnTrackTask(id=38, projectId=16, taskTitle=게임이 아냐 진 적이 없으니까, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T10:49:40, updatedAt=2024-06-06T10:49:40, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=40, projectId=16, taskTitle=인사이드 아웃2, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=null, hasChild=null, taskParentId=null, createdAt=2024-06-06T10:54:55, updatedAt=2024-06-06T10:54:55, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=42, projectId=16, taskTitle=왜 바로 추가되지 않을까?, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-14, hasChild=null, taskParentId=null, createdAt=2024-06-06T11:01:15, updatedAt=2024-06-06T11:01:15, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=43, projectId=16, taskTitle=js말고 바로 컨트롤러?, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T11:14:42, updatedAt=2024-06-06T11:14:42, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=57, projectId=16, taskTitle=톰과 크리스만, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T13:28:21, updatedAt=2024-06-06T13:28:21, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=60, projectId=16, taskTitle=공유만 선택, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-08, hasChild=null, taskParentId=null, createdAt=2024-06-06T13:40:35, updatedAt=2024-06-06T13:40:35, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=62, projectId=16, taskTitle=파일첨부안함, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T13:46:47, updatedAt=2024-06-06T13:46:47, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=65, projectId=16, taskTitle=무빙, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-07, hasChild=null, taskParentId=null, createdAt=2024-06-06T16:45:20, updatedAt=2024-06-06T16:45:20, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=66, projectId=16, taskTitle=redirectView와 fetch , authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-08, hasChild=null, taskParentId=null, createdAt=2024-06-06T16:48:35, updatedAt=2024-06-06T16:48:35, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=67, projectId=16, taskTitle=setTimeOut, authorMid=null, authorName=null, taskPriority=null, taskStatus=1, taskDueDate=2024-06-08, hasChild=null, taskParentId=null, createdAt=2024-06-06T16:54:12, updatedAt=2024-06-06T16:54:12, updatedBy=null, deletedBy=null, deletedAt=null)]
// , 14=[OnTrackTask(id=13, projectId=9, taskTitle=인생의 베일, authorMid=null, authorName=null, taskPriority=null, taskStatus=5, taskDueDate=2024-06-06, hasChild=null, taskParentId=null, createdAt=2024-05-29T15:44:42, updatedAt=2024-08-19T20:14:38, updatedBy=null, deletedBy=null, deletedAt=null), OnTrackTask(id=25, projectId=9, taskTitle=장 폴 사르트르의 말, authorMid=null, authorName=null, taskPriority=null, taskStatus=3, taskDueDate=2024-06-14, hasChild=null, taskParentId=null, createdAt=2024-06-05T21:23:16, updatedAt=2024-07-29T19:56:25, updatedBy=null, deletedBy=null, deletedAt=null)]}

    }
}