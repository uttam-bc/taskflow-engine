package com.gengar.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gengar.taskflow.dto.TaskCreateDto;
import com.gengar.taskflow.model.Task;
import com.gengar.taskflow.service.TaskService;
import com.gengar.taskflow.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies Presentation Layer constraints in isolation.
 * Uses @WebMvcTest which only instantiates the web tier (Controllers, Filters, @ControllerAdvice),
 * avoiding the heavy full ApplicationContext test.
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mocks the service layer instead of repository directly.
    @MockBean
    private TaskService taskService;

    @Test
    void createTask_ValidPayload_Returns201() throws Exception {
        TaskCreateDto dto = new TaskCreateDto("Test Task", "Test Desc");
        Task savedTask = new Task("Test Task", "Test Desc", "PENDING", Instant.now(), Instant.now());
        savedTask.setId("123");

        Mockito.when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createTask_InvalidTitle_Returns400WithRFC7807() throws Exception {
        TaskCreateDto dto = new TaskCreateDto("", "Test Desc"); // Blank generic title -> invalid

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:gengar:taskflow:problem:validation"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void getTaskById_NotFound_Returns404WithRFC7807() throws Exception {
        Mockito.when(taskService.getTaskById("999")).thenThrow(new TaskNotFoundException("999"));

        mockMvc.perform(get("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("urn:gengar:taskflow:problem:not-found"))
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Task not found with ID: 999"));
    }
}
