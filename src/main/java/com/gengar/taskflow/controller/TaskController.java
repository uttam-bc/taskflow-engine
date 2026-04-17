package com.gengar.taskflow.controller;

import com.gengar.taskflow.dto.TaskCreateDto;
import com.gengar.taskflow.dto.TaskResponseDto;
import com.gengar.taskflow.model.Task;
import com.gengar.taskflow.repository.TaskRepository;
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

    private final TaskRepository taskRepository;

    // Strict constructor injection. No internal Spring proxy magic like @Autowired on fields permitted here.
    // Throws a clear exception if dependency tree fails on startup.
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto createDto) {
        // Phase 1 Explicit mapping - bypassing mapstruct or ModelMapper for clear visibility
        Task task = new Task(
                createDto.getTitle(),
                createDto.getDescription(),
                "PENDING", // Default status
                Instant.now(), // Manual auditing lifecycle
                Instant.now()
        );

        Task savedTask = taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TaskResponseDto(savedTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasks() {
        List<TaskResponseDto> tasks = taskRepository.findAll().stream()
                .map(TaskResponseDto::new)
                .toList();
        return ResponseEntity.ok(tasks);
    }
}
