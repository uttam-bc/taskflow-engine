package com.gengar.taskflow.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * Task Entity.
 * Mapped to the "tasks" collection in MongoDB.
 * Explicitly separates Domain/Persistence logic from DTOs.
 */
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    // Explicitly mapping the field name in MongoDB. 
    // Creating an index to speed up title searches. In prod, auto-index-creation is explicitly DISABLED.
    @Field("title")
    @Indexed
    private String title;

    @Field("description")
    private String description;
    
    @Field("status")
    private String status;

    // Manual Lifecycle Hooks (Phase 1):
    // By skipping @CreatedDate and @LastModifiedDate (Auditing magic), we maintain explicit control.
    // The developer must explicitly populate these fields in Phase 1 to understand state transitions.
    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Required by Spring Data to instantiate the object via reflection when mapping results from MongoDB.
    public Task() {}

    public Task(String title, String description, String status, Instant createdAt, Instant updatedAt) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
