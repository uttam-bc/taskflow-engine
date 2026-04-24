package com.gengar.taskflow.service;

import com.gengar.taskflow.dto.TaskCreateDto;
import com.gengar.taskflow.exception.TaskNotFoundException;
import com.gengar.taskflow.model.Task;
import com.gengar.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Core business logic implementation for Task entity.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(TaskCreateDto createDto) {
        Task task = new Task(
                createDto.getTitle(),
                createDto.getDescription(),
                "PENDING",
                Instant.now(),
                Instant.now()
        );
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
