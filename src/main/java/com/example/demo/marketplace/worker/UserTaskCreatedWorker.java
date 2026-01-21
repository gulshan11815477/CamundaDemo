package com.example.demo.marketplace.worker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.demo.marketplace.entity.CamundaTaskCandidateGroup;
import com.example.demo.marketplace.entity.CamundaTaskEntity;
import com.example.demo.marketplace.repository.CamundaTaskCandidateGroupRepo;
import com.example.demo.marketplace.repository.CamundaTaskRepository;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserTaskCreatedWorker {

    private static final String HDR_USER_TASK_KEY = "io.camunda.zeebe:userTaskKey";
    private static final String HDR_CANDIDATE_GROUPS = "io.camunda.zeebe:candidateGroups";

    private final CamundaTaskRepository camundaTaskRepository;
    private final CamundaTaskCandidateGroupRepo camundaTaskCandidateGroupRepo;

    @Transactional
    @JobWorker(type = "persist-user-task")
    public void handleUserTaskCreated(final JobClient client, final ActivatedJob job) {

        Map<String, Object> vars = job.getVariablesAsMap();
        Map<String, String> headers = job.getCustomHeaders();

        String userTaskKeyHeader = headers.get(HDR_USER_TASK_KEY);
        if (userTaskKeyHeader == null || userTaskKeyHeader.isBlank()) {
            // missing key - nothing to persist
            return;
        }

        Long userTaskKey;
        try {
            userTaskKey = Long.valueOf(userTaskKeyHeader);
        } catch (NumberFormatException ex) {
            return;
        }

        Optional<CamundaTaskEntity> existing = camundaTaskRepository.findByCamundaTaskKey(userTaskKey);
        if (existing.isPresent()) {
            return;
        }

        CamundaTaskEntity task = new CamundaTaskEntity();
        task.setCamundaTaskId(userTaskKeyHeader);
        task.setCamundaTaskKey(userTaskKey);

        task.setProcessInstance(job.getProcessInstanceKey());
        task.setProcessDefinitionKey(Long.valueOf(job.getProcessDefinitionKey()));
        task.setBpmnElementId(job.getElementId());

        task.setName(job.getElementId());
        task.setAssignee(null);
        task.setStatus("CREATED");

        task.setCreatedAt(LocalDateTime.now());
        task.setCreatedBy("SYSTEM");

        task.setContractSummaryId(
                vars.get("contractSummaryId") != null ? Long.valueOf(vars.get("contractSummaryId").toString()) : null);

        task.setUiComponenetForm(vars.get("UiComponentKey") != null ? vars.get("UiComponentKey").toString() : null);

        CamundaTaskEntity savedCamundaTaskEntity = camundaTaskRepository.save(task);

        // save candidate group as pipe-separated list
        String pipeGroups = extractCandidateGroupsPipe(headers);
        if (pipeGroups != null) {
            CamundaTaskCandidateGroup camundaTaskCandidateGroup = new CamundaTaskCandidateGroup();
            camundaTaskCandidateGroup.setTaskId(savedCamundaTaskEntity);
            camundaTaskCandidateGroup.setGroupId(pipeGroups);
            camundaTaskCandidateGroupRepo.save(camundaTaskCandidateGroup);
        }

    }

    /**
     * Extracts candidate groups from header key "io.camunda.zeebe:candidateGroups".
     * Expected header format: ["202","203"]
     * Returns a single string with groups joined by '|' e.g. "202|203".
     */
    private String extractCandidateGroupsPipe(Map<String, String> headers) {
        String raw = headers.get(HDR_CANDIDATE_GROUPS);
        if (raw == null || raw.isBlank()) {
            return null;
        }

        List<String> groups = new ArrayList<>();
        Pattern p = Pattern.compile("\"(.*?)\"");
        Matcher m = p.matcher(raw);
        while (m.find()) {
            groups.add(m.group(1));
        }

        if (groups.isEmpty()) {
            return null;
        }
        return String.join("|", groups);
    }

}
