package com.gengar.taskflow.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String id) {
        super("Task not found with ID: " + id);
    }
}
