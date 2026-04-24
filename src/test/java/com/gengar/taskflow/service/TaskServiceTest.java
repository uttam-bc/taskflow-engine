package com.gengar.taskflow.service;

import com.gengar.taskflow.dto.TaskCreateDto;
import com.gengar.taskflow.exception.TaskNotFoundException;
import com.gengar.taskflow.model.Task;
import com.gengar.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_ValidDto_ReturnsSavedTask() {
        TaskCreateDto dto = new TaskCreateDto("Service Task", "Desc");
        Task mockSavedTask = new Task("Service Task", "Desc", "PENDING", Instant.now(), Instant.now());
        mockSavedTask.setId("1");

        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(mockSavedTask);

        Task result = taskService.createTask(dto);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Service Task", result.getTitle());
        assertEquals("PENDING", result.getStatus());
        Mockito.verify(taskRepository, Mockito.times(1)).save(any(Task.class));
    }

    @Test
    void getAllTasks_ReturnsList() {
        Task t1 = new Task("T1", "D1", "PENDING", Instant.now(), Instant.now());
        Mockito.when(taskRepository.findAll()).thenReturn(List.of(t1));

        List<Task> results = taskService.getAllTasks();

        assertEquals(1, results.size());
        assertEquals("T1", results.get(0).getTitle());
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask() {
        Task t1 = new Task("T1", "D1", "PENDING", Instant.now(), Instant.now());
        t1.setId("123");
        Mockito.when(taskRepository.findById("123")).thenReturn(Optional.of(t1));

        Task result = taskService.getTaskById("123");

        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void getTaskById_NonExistingId_ThrowsException() {
        Mockito.when(taskRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById("999"));
    }
}
