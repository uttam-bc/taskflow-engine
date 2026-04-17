package com.gengar.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskFlowApplication {

    public static void main(String[] args) {
        // Initializes the Spring ApplicationContext. 
        // Bootstraps auto-configuration based on classpath entries (e.g., MongoAutoConfiguration, WebMvcAutoConfiguration).
        SpringApplication.run(TaskFlowApplication.class, args);
    }
}
