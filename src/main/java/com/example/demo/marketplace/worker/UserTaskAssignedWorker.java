package com.example.demo.marketplace.worker;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Component;

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
public class UserTaskAssignedWorker {

	private final CamundaTaskRepository camundaTaskRepository;
	private final CamundaTaskCandidateGroupRepo camundaTaskCandidateGroupRepo;

	@Transactional
	@JobWorker(type = "assign-user-task", autoComplete = true)
	public void handleTaskAssignment(final JobClient client, final ActivatedJob job) {

		Map<String, Object> vars = job.getVariablesAsMap(); // only UiComponentKey here
		Map<String, String> headers = job.getCustomHeaders();

		String userTaskKeyStr = headers.get("io.camunda.zeebe:userTaskKey");
		if (userTaskKeyStr == null) {
			throw new RuntimeException("Missing io.camunda.zeebe:userTaskKey header");
		}

		Long camundaTaskKey = Long.valueOf(userTaskKeyStr);

		String assignee = headers.get("io.camunda.zeebe:assignee"); // "101"

		// ---------------- idempotent update ----------------

		CamundaTaskEntity task = camundaTaskRepository.findByCamundaTaskKey(camundaTaskKey)
				.orElseThrow(() -> new RuntimeException("Task not found for key=" + camundaTaskKey));

		if ("CLAIMED".equals(task.getStatus())) {
//			log.info("Task already claimed. camundaTaskKey={}", camundaTaskKey);
			return;
		}

		// ---------------- apply updates ----------------

		task.setAssignee(assignee); // "101"
		task.setStatus("CLAIMED");
		task.setClaimedAt(LocalDateTime.now());

		camundaTaskRepository.save(task);
	}

	private String getString(Map<String, Object> vars, String key) {
		return vars.get(key) != null ? vars.get(key).toString() : null;
	}

	private Long getLong(Map<String, Object> vars, String key) {
		return vars.get(key) != null ? Long.valueOf(vars.get(key).toString()) : null;
	}
}
