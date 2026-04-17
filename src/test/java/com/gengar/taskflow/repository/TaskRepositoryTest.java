package com.gengar.taskflow.repository;

import com.gengar.taskflow.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DataMongoTest instantiates the Data Tier dependencies ONLY.
 * Testcontainers ensures we boot a REAL MongoDB instance via Docker, not an in-memory fake, matching production query planning.
 */
@Testcontainers
@DataMongoTest
class TaskRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4").withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    void cleanUp() {
        taskRepository.deleteAll();
    }

    @Test
    void saveAndFindTask_Success_PersistsToDB() {
        // Arrange
        Task task = new Task("Container Task", "Test", "PENDING", Instant.now(), Instant.now());
        
        // Act
        Task saved = taskRepository.save(task);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(taskRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void findByStatus_ReturnsOnlyMatchingTasks() {
        // Arrange
        taskRepository.save(new Task("A", "Desc", "DONE", Instant.now(), Instant.now()));
        taskRepository.save(new Task("B", "Desc", "PENDING", Instant.now(), Instant.now()));

        // Act
        List<Task> pendingTasks = taskRepository.findByStatus("PENDING");

        // Assert
        assertThat(pendingTasks).hasSize(1);
        assertThat(pendingTasks.get(0).getTitle()).isEqualTo("B");
    }
}
