package com.example.demo.marketplace.controller;

import java.util.Map;

import com.example.demo.marketplace.service.MarketplaceTasklistApiClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marketplace/camunda")
@RequiredArgsConstructor
public class CamundaTaskApiController {


    private final MarketplaceTasklistApiClient tasklistApiClient;
    @PostMapping("/v2/user-tasks/{taskId}/assignment")
    public void assignTask(@PathVariable String taskId, @RequestBody Map<String, Object> assignMap) {
        tasklistApiClient.assignTask(taskId, assignMap);

    }

    // 6. Complete task
    @PostMapping("/v2/user-tasks/{taskId}/completion")
    public void completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        tasklistApiClient.completeTask(taskId, variables);
    }
}
