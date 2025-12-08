package com.example.demo.model;

import java.time.Instant;

import lombok.Data;

@Data
public class TaskItem {
    private String id;
    private String name;
    private String taskState;
    private String assignee;
    private String processInstanceId;
    private Instant creationTime;

    // getters + setters
}
