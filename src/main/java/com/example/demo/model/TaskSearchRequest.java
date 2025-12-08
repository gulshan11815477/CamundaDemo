package com.example.demo.model;

import java.time.Instant;

import lombok.Data;

@Data
public class TaskSearchRequest {
    private int page = 0;
    private int pageSize = 50;
    private String assignee;
    private String state;         // CREATED, COMPLETED
    private Instant createdAfter;

}
