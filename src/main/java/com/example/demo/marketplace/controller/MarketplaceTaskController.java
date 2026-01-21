package com.example.demo.marketplace.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.marketplace.entity.CamundaTaskEntity;
import com.example.demo.marketplace.entity.CamundaTaskFormEntity;
import com.example.demo.marketplace.service.MarketplaceTaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marketplace/tasks")
@RequiredArgsConstructor
public class MarketplaceTaskController {

    private final MarketplaceTaskService taskService;

    // Search tasks with optional filters (camundaTaskKey, assignee, status)
    @GetMapping
    public List<CamundaTaskEntity> searchTasks(@RequestParam(required = false) Long camundaTaskKey,
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String status) {
        return taskService.searchTasks(camundaTaskKey, assignee, status);
    }

    // Search tasks by candidate group id (matches stored pipe-separated groupId), optional status
    @GetMapping("/by-group")
    public List<CamundaTaskEntity> searchTasksByCandidateGroup(@RequestParam String candidateGroupId,
            @RequestParam(required = false) String status) {
        return taskService.searchTasksByCandidateGroup(candidateGroupId, status);
    }

    // Get task form by id
    @GetMapping("/getUiFormId")
    public ResponseEntity<?> getUiFormId(@RequestParam Long camundaTaskKey) {


        return ResponseEntity.ok(Map.of("uiComponentForm", taskService.getUiFormId(camundaTaskKey)));

    }

    // Get task by camundaTaskKey
    @GetMapping("/by-key")
    public ResponseEntity<CamundaTaskEntity> getTaskByKey(@RequestParam Long camundaTaskKey) {
        return taskService.findByCamundaTaskKey(camundaTaskKey).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
