package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Slf4j
@SpringBootTest
class ProjectServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TaskRepository taskRepository;

    @Test @DisplayName("내 프로젝트 목록 조회: 특정 유저 id로, project member에서 먼저 project id 뽑아온 버전")
    void allMyProjects1() {
        Long userId = 36L;
/*
        // 1. project member테이블에서 내 userId와 동일한 row에 있는 project(id+)를 가져온다.
        List<ProjectMember> myProjects = memberRepository.findProjectsByUserId(userId);
        log.info("myProjects: {}", myProjects);
        // myProjects: [ProjectMember(id=2, projectId=8, userId=null, nickname=null, position=creator, capacity=null, joinedAt=2024-05-21)]
        // List 타입이 아닐 때
//        List<MyProject> test = myProjects.stream()
//                .map(p -> projectRepository.allMyProjects(p.getProjectId()))
//                .collect(Collectors.toList());
//        log.info("test: {}", test);
        // test: [MyProject(projectId=8, projectType=solo, projectStatus=activated, creatorId=36, creatorName=베토벤, projectName=베토벤의 첫 프로젝트, createdAt=2024-05-21, updatedAt=2024-05-21, memberId=null, position=null)]
        List<Long> projectsIds = myProjects.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());

        log.info("projects ids: {}", projectsIds);
        // projects ids: [8]


        // 2. 프로젝트를 목록에 담는다. ver.1.
//        List<OnTrackProject> projectList = new ArrayList<>();
//        for(int i = 0; i < projectsIds.size(); i++){
//            Optional<OnTrackProject> project = projectRepository.findByProjectId(projectsIds.get(i));
//            projectList.add(project.get());
//        }


        // 2. 프로젝트를 목록에 담는다. ver.2.
        List<MyProject> projectList = new ArrayList<>();
        for (Long projectsId : projectsIds) {
//            projectList.add(projectRepository.allMyProjects(projectsId));
        }
        log.info("projectList: {}", projectList); */
        // projectList: [MyProject(projectId=8, projectType=solo, projectStatus=activated, creatorId=36, creatorName=베토벤, projectName=베토벤의 첫 프로젝트, createdAt=2024-05-21, updatedAt=2024-05-21, memberId=null, position=null)]
    } // allMyProjects1() ends
    
    @Test @DisplayName("내 모든 프로젝트: 리스트로 먼저 뽑고, 생성자 별명 따로 뽑아오는 버전")
    void allMyProjects(){
        List<MyProject> pl = projectRepository.allMyProjects(35L);
        log.info("list로 어떻게 나오나?: {}", pl);
        //list로 어떻게 나오나?:
        // [MyProject(projectId=9, projectType=team, projectStatus=activated, creatorId=35,
        // creatorName=null, projectName=By your side, projectDueDate=null, createdAt=2024-05-22,
        // updatedAt=2024-05-22, memberId=14, position=creator, invitedAt=null),
        // MyProject(projectId=16, projectType=team, projectStatus=activated, creatorId=42,
        // creatorName=null, projectName=Shut down, projectDueDate=null, createdAt=2024-05-22,
        // updatedAt=2024-05-22, memberId=25, position=member, invitedAt=null)]

        // 생성자들(혹은 멤버들)의 이름 가져오기

        // 1. 생성자들 id 뽑아오기
        List<Long> creators = pl.stream().map(MyProject::getCreatorId).collect(Collectors.toList());
        log.info("creators: {}", creators); // creators: [35, 42]

        // 2. 생성자 id와 해당 인덱스를 map으로 뽑아온다. (추후 생성자 '이름' 넣기 쉽도록?)
        Map<Long, Integer> idxOfCreator = new HashMap<>();
//        for(int i = 0; i < pl.size(); i++){
//            for (Long creator : creators) {
//                if (Objects.equals(pl.get(i).getCreatorId(), creator)) {
//                    idxOfCreator.put(creator, i);
//                }
//            }
//        }
        IntStream.range(0, pl.size()).forEach(i -> {
            for (Long creator : creators) {
                if (Objects.equals(pl.get(i).getCreatorId(), creator)) {
                    idxOfCreator.put(creator, i);
                }
            }
        });
        log.info("생성자의 id와 그 id의 위치(index): {}", idxOfCreator);
        // 생성자의 user id와 그 id의 위치(index): {0=35, 1=42} >> {35=0, 42=1} user id: 35 = 공지철, user id: 42 = 크리스 에반스

//        Map<Long, Long> creatorList = pl.stream().collect(toUnmodifiableMap(MyProject::getProjectId, MyProject::getCreatorId));
//        log.info("Map이 됐나?: {}", creatorList); // Map이 됐나?: {16=42, 9=35} 프로젝트id=userId(creatorId)

        List<GetMemberNameRequest> reqList = new ArrayList<>();

        for (MyProject projectList : pl) {
            GetMemberNameRequest request = GetMemberNameRequest.builder()
                    .projectId(projectList.getProjectId())
                    .userId(projectList.getCreatorId())
                    .build();
            reqList.add(request);
        }

        // 3. 생성자의 이름 받아오기
        List<MemberInfo> mnn = new ArrayList<>();
        IntStream.range(0, pl.size()).forEach(i -> mnn.add(projectRepository.getMemberInfo(reqList.get(i)).get(0)));

        IntStream.range(0, mnn.size()).forEach(i -> {
            // 생성자 id가 키값인 map에서 해당 id의 인덱스를 가져온다.
            // 해당 인덱스에 생성자의 이름을 넣는다.
            pl.get(idxOfCreator.get(mnn.get(i).getUserId())).setCreatorName(mnn.get(i).getNickName());
        });

        log.info("생성자 이름까지 완성된 projectList: {}", pl);
        //[MyProject(projectId=9, projectType=team, projectStatus=activated, creatorId=35, creatorName=공지철,
        // projectName=By your side, projectDueDate=null, createdAt=2024-05-22, updatedAt=2024-05-22,
        // memberId=14, position=creator, invitedAt=null),
        // MyProject(projectId=16, projectType=team, projectStatus=activated, creatorId=42, creatorName=크리스 에반스,
        // projectName=Shut down, projectDueDate=null, createdAt=2024-05-22, updatedAt=2024-05-22,
        // memberId=25, position=member, invitedAt=null)]
    }

    @Test @DisplayName("프로젝트 가져오기")
    void getProject() {
        Long projectId = 9L;
        ProjectResponse project = ProjectResponse.builder().build();
        // 1. 프로젝트 정보
        // id, CREATOR, project_name, project_type, project_status, project_url, project_dueDate, createdAt, updatedAt
        project.setProject(projectRepository.findByProjectId(projectId));

        // 2. 프로젝트 소속 멤버 정보
        // id as memberId, user_id, project_id, nickname
        project.setMemberList(projectRepository.getMemberInfo(GetMemberNameRequest.builder().projectId(projectId).build()));

        // 3-1. 프로젝트 내 할 일 목록
        project.setTaskList(projectRepository.allTasksInProject(projectId));

        // 3-2. 할 일 별 담당자 목록
        IntStream.range(0, project.getTaskList().size()).forEach(i -> {
            List<TaskAssignment> assigneeList = taskRepository.getAssigneeList(project.getTaskList().get(i).getId());

            List<String> assigneeNames = new ArrayList<>();
            List<Long> assigneeIds = new ArrayList<>();
            for (TaskAssignment taskAssignment : assigneeList) {
                assigneeNames.add(taskAssignment.getNickname());
                assigneeIds.add(taskAssignment.getMemberId());
            }
            project.getTaskList().get(i).setAssigneeNames(assigneeNames);
            project.getTaskList().get(i).setAssigneeMids(assigneeIds);
        });

        // 4. 프로젝트 멤버별 할 일 목록
        // 4-1. 공동task 여부 확인
        List<AssignmentList> aList = new ArrayList<>();
        IntStream.range(0, project.getMemberList().size()).forEach(i -> {
            AssignmentList assignment = AssignmentList.builder()
                    .assigneeMid(project.getMemberList().get(i).getMemberId())
                    .assigneeName(project.getMemberList().get(i).getNickName())
                    .tList(taskRepository.getAssigneeView(project.getMemberList().get(i).getMemberId()))
                    .build();
            aList.add(assignment);
        });
        project.setAssignmentList(aList);

        log.info("프로젝트 정보, 멤버 목록, 할 일 목록, 할일별 담당자목록: {}", project);
        // 프로젝트 정보, 멤버 목록, 할 일 목록, 할일별 담당자목록:
        // ProjectResponse(project=OnTrackProject(id=9, creator=35, projectType=team, projectName=By your side,
        // projectUrl=0c49b029-56dc-40ff-bedc-6d25cb86fc36, projectStatus=activated, projectDueDate=null,
        // createdAt=2024-05-22T11:19:10, updatedAt=2024-05-22T11:19:10),
        //
        // memberInfo=[MemberInfo(userId=45, projectId=9, memberId=4, nickname=Adele),
        // MemberInfo(userId=35, projectId=9, memberId=14, nickname=공지철),
        // MemberInfo(userId=61, projectId=9, memberId=26, nickname=송혜교),
        // MemberInfo(userId=47, projectId=9, memberId=27, nickname=크러쉬),
        // MemberInfo(userId=50, projectId=9, memberId=28, nickname=스칼렛 요한슨)],
        //
        // taskList=[TaskList(id=8, taskTitle=Tigger can do everything, authorMid=14, authorName=공지철,
        // taskPriority=norm, taskStatus=ing, taskDueDate=null, taskParentId=null, createdAt=2024-05-24T12:56:29,
        // updatedAt=2024-05-24T12:56:29, updatedBy=14, assigneeMids=[4, 26, 27, 28],
        // assigneeNames=[Adele, 송혜교, 크러쉬, 스칼렛 요한슨]),
        // TaskList(id=9, taskTitle=경복궁 야간개방, authorMid=14, authorName=공지철, taskPriority=norm,
        // taskStatus=planning, taskDueDate=null, taskParentId=null, createdAt=2024-05-28T12:13:54,
        // updatedAt=2024-05-28T12:13:54, updatedBy=14, assigneeMids=[26], assigneeNames=[송혜교]),
        // TaskList(id=10, taskTitle=그 벌들은 다 어디로 갔을까, authorMid=4, authorName=Adele, taskPriority=norm,
        // taskStatus=not-yet, taskDueDate=2024-05-31, taskParentId=null, createdAt=2024-05-28T12:17:53,
        // updatedAt=2024-05-28T12:17:53, updatedBy=4, assigneeMids=[27], assigneeNames=[크러쉬]),
        // TaskList(id=11, taskTitle=Deep Time, authorMid=26, authorName=송혜교, taskPriority=ip, taskStatus=not-yet,
        // taskDueDate=2024-05-29, taskParentId=null, createdAt=2024-05-29T15:36:42, updatedAt=2024-05-29T15:36:42,
        // updatedBy=26, assigneeMids=[28], assigneeNames=[스칼렛 요한슨]),
        // TaskList(id=12, taskTitle=2시탈출 컬투쇼, authorMid=26, authorName=송혜교, taskPriority=ip, taskStatus=review,
        // taskDueDate=2024-05-29, taskParentId=null, createdAt=2024-05-29T15:41:36, updatedAt=2024-05-29T15:41:36,
        // updatedBy=26, assigneeMids=[4, 28], assigneeNames=[Adele, 스칼렛 요한슨]),
        // TaskList(id=13, taskTitle=인생의 베일, authorMid=28, authorName=스칼렛 요한슨, taskPriority=ip,
        // taskStatus=done, taskDueDate=2024-06-06, taskParentId=null, createdAt=2024-05-29T15:44:42,
        // updatedAt=2024-05-29T15:44:42, updatedBy=28, assigneeMids=[14, 26], assigneeNames=[공지철, 송혜교])],
        //
        // assignmentList=[AssignmentList(assigneeMid=4, assigneeName=Adele,
        // tList=[AssigneeTaskList(id=8, taskTitle=Tigger can do everything, taskStatus=ing, taskDueDate=null, assigneeNum=4),
        // AssigneeTaskList(id=12, taskTitle=2시탈출 컬투쇼, taskStatus=review, taskDueDate=2024-05-29, assigneeNum=2)]),

        // AssignmentList(assigneeMid=14, assigneeName=공지철,
        // tList=[AssigneeTaskList(id=13, taskTitle=인생의 베일, taskStatus=done, taskDueDate=2024-06-06, assigneeNum=2)]),

        // AssignmentList(assigneeMid=26, assigneeName=송혜교,
        // tList=[AssigneeTaskList(id=8, taskTitle=Tigger can do everything, taskStatus=ing, taskDueDate=null, assigneeNum=4),
        // AssigneeTaskList(id=9, taskTitle=경복궁 야간개방, taskStatus=planning, taskDueDate=null, assigneeNum=1),
        // AssigneeTaskList(id=13, taskTitle=인생의 베일, taskStatus=done, taskDueDate=2024-06-06, assigneeNum=2)]),
        //
        // AssignmentList(assigneeMid=27, assigneeName=크러쉬,
        // tList=[AssigneeTaskList(id=8, taskTitle=Tigger can do everything, taskStatus=ing, taskDueDate=null, assigneeNum=4),
        // AssigneeTaskList(id=10, taskTitle=그 벌들은 다 어디로 갔을까, taskStatus=not-yet, taskDueDate=2024-05-31, assigneeNum=1)]),
        //
        // AssignmentList(assigneeMid=28, assigneeName=스칼렛 요한슨,
        // tList=[AssigneeTaskList(id=8, taskTitle=Tigger can do everything, taskStatus=ing, taskDueDate=null, assigneeNum=4),
        // AssigneeTaskList(id=11, taskTitle=Deep Time, taskStatus=not-yet, taskDueDate=2024-05-29, assigneeNum=1),
        // AssigneeTaskList(id=12, taskTitle=2시탈출 컬투쇼, taskStatus=review, taskDueDate=2024-05-29, assigneeNum=2)])])
    }
}