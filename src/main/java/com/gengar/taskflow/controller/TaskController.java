package com.gengar.taskflow.controller;

import com.gengar.taskflow.dto.TaskCreateDto;
import com.gengar.taskflow.dto.TaskResponseDto;
import com.gengar.taskflow.model.Task;
import com.gengar.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * Presentation Layer for Task endpoints.
 * 
 * INTERNAL BEHAVIOR:
 * 1. DispatcherServlet routes requests here based on @RequestMapping.
 * 2. HandlerMethodArgumentResolver intercepts @RequestBody.
 * 3. Before reaching the method, RequestResponseBodyMethodProcessor combined with ValidatorImpl runs bean validation (@Valid).
 *    If invalid, it throws MethodArgumentNotValidException (handled globally).
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    // Strict constructor injection. No internal Spring proxy magic like @Autowired on fields permitted here.
    // Throws a clear exception if dependency tree fails on startup.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto createDto) {
        Task savedTask = taskService.createTask(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TaskResponseDto(savedTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks().stream()
                .map(TaskResponseDto::new)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(new TaskResponseDto(task));
    }
}
