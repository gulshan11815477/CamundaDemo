package com.example.demo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.marketplace.entity.CamundaTaskEntity;
import com.example.demo.marketplace.entity.CamundaTaskFormEntity;

public interface MarketplaceTaskService {

    // Replace candidateGroup parameter with status to allow filtering by task status
    List<CamundaTaskEntity> searchTasks(Long camundaTaskKey, String assignee, String status);

    // New: search by candidate group id (matches stored pipe-separated groupId)
    List<CamundaTaskEntity> searchTasksByCandidateGroup(String candidateGroupId, String status);

    Optional<CamundaTaskEntity> findByCamundaTaskKey(Long camundaTaskKey);

    String getUiFormId(Long camundaTaskKey);

    CamundaTaskEntity save(CamundaTaskEntity task);

    void assignTask(Long camundaTaskKey, String assignee);

    void completeTask(Long camundaTaskKey, String completedBy);

}
