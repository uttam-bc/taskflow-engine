package com.gengar.taskflow.service;

import com.gengar.taskflow.dto.TaskCreateDto;
import com.gengar.taskflow.model.Task;

import java.util.List;

/**
 * Service Layer Boundary for Task operations.
 */
public interface TaskService {
    Task createTask(TaskCreateDto createDto);
    List<Task> getAllTasks();
    Task getTaskById(String id);
}
