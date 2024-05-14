package me.jhchoi.ontrack.controller;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.dto.AddTaskRequest;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/addTask")
    public String addTask(@RequestBody AddTaskRequest addTaskRequest){
        return null;
    }

}
