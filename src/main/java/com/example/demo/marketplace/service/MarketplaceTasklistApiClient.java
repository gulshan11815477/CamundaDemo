package com.example.demo.marketplace.service;

import java.util.Map;

public interface MarketplaceTasklistApiClient {

    void assignTask(String taskId, Map<String, Object> assignMap);

    void completeTask(String taskId, Map<String, Object> variables);

}
