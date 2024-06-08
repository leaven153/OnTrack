package me.jhchoi.ontrack.service;

import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.TaskAssignment;
import me.jhchoi.ontrack.domain.TaskHistory;
import me.jhchoi.ontrack.dto.TaskFormRequest;
import me.jhchoi.ontrack.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
class TaskServiceTest {

    @Autowired
    TaskRepository taskRepository;

    /**
     * test용 더미: project id: 9, user id: 35(공지철), creator's member id:14, project name: By your side
     * member(creator 1, member 4):
     *  공지철(member id: 14, user id: 35, CREATOR)
     *  Adele(member id: 4, user id: 45)
     *  송혜교(member id: 26, user id: 61)
     *  크러쉬(member id: 27, user id: 47)
     *  스칼렛 요한슨(member id: 28, user id: 50)
     *  task title: "매일 두유", "Tigger can do everything", "경복궁 야간개방", "Deep Time"
     * */
    @Test
    @DisplayName("새 할 일 등록: 담당자가 있고 파일 없는 버전")
    void addTaskWithAssigneeNoFile() {

        Long[] memberIds = {14L, 26L}; //4L, 26L, 27L, 28L, 14L
        String[] nicknames = {"공지철", "송혜교"}; // "Adele", "송혜교", "크러쉬", "스칼렛 요한슨", "공지철"
        String[] titles = {"그 벌들은 다 어디로 갔을까", "Tigger can do everything", "경복궁 야간개방", "Deep Time", "2시탈출 컬투쇼", "인생의 베일", "우리 몸 안내서"};
        TaskFormRequest taskFormRequest = TaskFormRequest.builder()
                .projectId(9L)
                .taskAuthorMid(28L)
                .authorName("스칼렛 요한슨")
                .taskTitle(titles[5])
                .taskPriority(2)
                .assigneesMid(List.of(memberIds))
                .assigneeNames(List.of(nicknames))
                .taskDueDate(LocalDate.now())
                .build();

        OnTrackTask task = taskFormRequest.dtoToEntityTask();

        taskRepository.newTask(task);

        taskRepository.log(TaskHistory.logNewTask(task));

        if (taskFormRequest.getAssigneesMid().size() > 0) {

            // 3-1. 담당자 객체(TaskAssignment) 생성 및 DB 저장
            List<TaskAssignment> assignees = taskFormRequest.dtoToEntityTaskAssignment(task.getId(), task.getCreatedAt());
            log.info("assignees 만들어진 상태: {}", assignees);
            taskRepository.assign(assignees);

            // 3-2. history 등록 - ② 담당자 인원만큼 history 객체 생성 및 DB 저장
            IntStream.range(0, assignees.size()).forEach(i -> taskRepository.log(TaskHistory.logAssignment(assignees.get(i), 14L)));
        }
    }

    @Test @DisplayName("담당자와 파일 모두 있는 버전")
    void addTask(){

    }

}