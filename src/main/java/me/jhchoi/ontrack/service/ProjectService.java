package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackProject;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.dto.*;
import me.jhchoi.ontrack.repository.MemberRepository;
import me.jhchoi.ontrack.repository.ProjectRepository;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;


    /**
     * created  : 24-05-21
     * param    : OnTrackProject, ProjectMember
     * return   : void
     * explain  : 새 프로젝트 등록, 해당 프로젝트의 생성자(ProjectMember 테이블)로 등록
     * */
    public void createProject(OnTrackProject newProj, ProjectMember creator){
        log.info("===============project 생성 service 접근===============");
        projectRepository.save(newProj);
        creator.setProjectId(newProj.getId());
        memberRepository.joinProject(creator);
    }

    /**
     * created  : 24-05-21
     * updated  : 24-05-22
     * param    : userId
     * return   : List<ProjectList>
     * explain  : 내 모든 프로젝트 목록 조회(my page)
     * */
    public List<ProjectList> allMyProjects(Long userId){

        log.info("===============project 목록조회 service 접근===============");
        // 1. 해당 유저가 속한 프로젝트 목록 
        List<ProjectList> projectList =projectRepository.allMyProjects(userId);

        // 2. 해당 프로젝트 생성자(들)의 user id 목록
        List<Long> creators = projectList.stream().map(ProjectList::getCreatorId).toList();

        // 3. 생성자 id의 (project list 내에서의 )idx 가져오기(map으로 생성자의 user id를 key으로 하여 index를 value로 매핑)
        Map<Long, Integer> idxOfCreator = new HashMap<>();
        IntStream.range(0, projectList.size()).forEach(i -> {
            for (Long creator : creators) {
                if (Objects.equals(projectList.get(i).getCreatorId(), creator)) {
                    idxOfCreator.put(creator, i);
                }
            }
        });

        // 4. 생성자가 2개 이상의 프로젝트를 가지고 있고, 각각의 프로젝트에서 다른 nickname을 사용할 수 있으므로
        // 생성자의 user id와 project id가 일치하는 row의 nickname을 받아오기 위해
        // 전용 dto 생성
        // (ontrack_project 테이블의 creator 컬럼에는 creator의 user id가 저장되어 있다.
        // ∵ 프로젝트를 만들고 나서 project id를 가지고
        // project member에 추가하기 때문에 member id가 들어갈 수 없다.)
        List<GetMemberNameRequest> reqList = new ArrayList<>();
        for (ProjectList list : projectList) {
            GetMemberNameRequest request = GetMemberNameRequest.builder()
                    .projectId(list.getProjectId())
                    .userId(list.getCreatorId())
                    .build();
            reqList.add(request);
        }

        // 5. 생성자 이름 data요청
        List<MemberList> mnn = new ArrayList<>();
        IntStream.range(0,projectList.size()).forEach(i -> mnn.add(projectRepository.getMemberList(reqList.get(i)).get(0)));

        // 6. 생성자의 이름을 project List에 매칭하여 저장
        IntStream.range(0, mnn.size()).forEach(i -> {
            // 생성자 id가 키값(mnn.get(i).getUserId())인 map(idxOfCreator)에서
            // value를 가져오면 그것은 생성자의 이름이 들어갈 위치!
            // 해당 인덱스에 생성자의 이름을 넣는다.
            projectList.get(idxOfCreator.get(mnn.get(i).getUserId())).setCreatorName(mnn.get(i).getNickName());
        });

        return projectList;
    }

    /**
     * started  : 24-05-27
     * complete : 24-05-
     * param    :
     * return   :
     * explain  : 프로젝트 상세(프로젝트 정보, 멤버목록, 할 일 목록)
     * */
    public ProjectResponse getProject(Long projectId){

        ProjectResponse project = ProjectResponse.builder().build();
        // 1. 프로젝트 정보
        // id, CREATOR, project_name, project_type, project_status, project_url, project_dueDate, createdAt, updatedAt
        project.setProject(projectRepository.findByProjectId(projectId));

        // 2. 프로젝트 소속 멤버 정보  → MemberList
        // id as memberId, user_id, project_id, nickname
        project.setMemberList(projectRepository.getMemberList(GetMemberNameRequest.builder().projectId(projectId).build()));

        // 3-1. 프로젝트 내 할 일 목록 (from ontrack_task) → TaskList
        // id, task_title, task_status, task_dueDate, task_priority, author, createdAt, updatedAt
        project.setTaskList(projectRepository.allTasksInProject(projectId));

        // 3-2. 할 일 별 담당자 목록 (from task_assignment) → TaskList
        // task id로 assignee list를 가져온다.
        // assingee 수 만큼 task list에 add한다.
        IntStream.range(0, project.getTaskList().size()).forEach(i -> {
          List<TaskAssignment> assigneeList = taskRepository.getAssigneeList(project.getTaskList().get(i).getId());
            List<String> assigneeNames = new ArrayList<>();
            List<Long> assigneeIds = new ArrayList<>();
            Map<Long, String> assignees = new HashMap<>();
            for (TaskAssignment taskAssignment : assigneeList) {
                assigneeNames.add(taskAssignment.getNickname());
                assigneeIds.add(taskAssignment.getMemberId());
                assignees.put(taskAssignment.getMemberId(), taskAssignment.getNickname());
            }
            project.getTaskList().get(i).setAssigneeNames(assigneeNames);
            project.getTaskList().get(i).setAssigneeMids(assigneeIds);
            project.getTaskList().get(i).setAssignees(assignees);
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


//        log.info("불려온 project: {}", project);
        return project;
    }

    /**
     * created  : 24-05
     * param    :
     * return   :
     * explain  : 프로젝트 초대 수락(= 내 프로젝트 목록에 추가)
     * */
    public void acceptInvitation(ResponseInvitation newCrew){
        // project id와 user id 받아서
        // project member update
        // ① nickname에 user name 입력
        // ② position을 invitedAs값으로 변경
        // ③ joinedAt에 날짜 입력
        memberRepository.acceptInvitation(newCrew);

    }
}
