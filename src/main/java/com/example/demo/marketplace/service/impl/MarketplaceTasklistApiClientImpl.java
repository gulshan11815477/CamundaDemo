package com.example.demo.marketplace.service.impl;

import com.example.demo.marketplace.repository.CamundaTaskRepository;
import com.example.demo.marketplace.service.MarketplaceTasklistApiClient;
import com.example.demo.service.TasklistApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketplaceTasklistApiClientImpl implements MarketplaceTasklistApiClient {

    private final TasklistApiClient delegate;
    private final CamundaTaskRepository camundaTaskRepository;

    @Override
    public void assignTask(String taskId, Map<String, Object> variables) {
        // 1) call external Tasklist API to assign
        delegate.assignTask(taskId, variables);

        // 2) update DB. If DB update fails, attempt compensation by unassigning externally
        String assignee = variables.get("assignee") != null ? variables.get("assignee").toString() : null;
        try {
            updateTaskStatusToClaimedInDb(taskId, assignee);
        } catch (RuntimeException dbEx) {
            // attempt compensation: unassign via tasklist API
            try {
                delegate.unassignTask(taskId);
            } catch (Exception ex) {
                // both DB update failed and compensation failed — escalate
                throw new RuntimeException("Failed to update DB and failed to compensate external assignment", ex);
            }
            // after compensation, rethrow original DB exception to signal failure
            throw dbEx;
        }
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        delegate.completeTask(taskId, variables);

        //Update DB also ,update sttaus to "COMPLETED"

        String assignee = variables.get("assignee") != null ? variables.get("assignee").toString() : null;
        int updated = camundaTaskRepository.completeTask(taskId, assignee, "COMPLETED");
        if (updated == 0) {
            // no rows updated → either task not found or concurrent modification
            throw new RuntimeException("Failed to update task status to COMPLETED in DB for camundaTaskId: " + taskId);
        }
    }

    // Separate transactional method that updates the CamundaTaskEntity status to CLAIMED
    // Use repository's modifying query for atomic update; this method is transactional
    @Transactional
    public void updateTaskStatusToClaimedInDb(String camundaTaskId, String assignee) {
        if (camundaTaskId == null || camundaTaskId.isBlank()) {
            throw new IllegalArgumentException("camundaTaskId is required");
        }

        int updated = camundaTaskRepository.updateAssigneeAndStatusByCamundaTaskId(camundaTaskId, assignee, "CLAIMED");
        if (updated == 0) {
            // no rows updated → either task not found or concurrent modification
            throw new RuntimeException("Failed to update task in DB for camundaTaskId: " + camundaTaskId);
        }
    }
}
