package com.gengar.taskflow.dto;

import com.gengar.taskflow.model.Task;
import java.time.Instant;

/**
 * Ensures we only expose specific fields to the consumer, not the full DB shape.
 */
public class TaskResponseDto {
    private String id;
    private String title;
    private String description;
    private String status;
    private Instant createdAt;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.createdAt = task.getCreatedAt();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
