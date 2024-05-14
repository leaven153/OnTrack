package me.jhchoi.ontrack.controller;

import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.service.TaskService;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

}
