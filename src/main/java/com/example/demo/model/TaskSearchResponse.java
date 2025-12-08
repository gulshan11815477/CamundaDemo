package com.example.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class TaskSearchResponse {
    private List<TaskItem> items;

    // getters + setters
}
