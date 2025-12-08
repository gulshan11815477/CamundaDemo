package com.example.demo.worker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskFormEntity;
import com.example.demo.repository.TaskFormRepo;
import com.example.demo.repository.TaskRepo;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class UserTaskWorker {

	@Autowired
	RestTemplate restTemplate;

	private final TaskRepo taskRepo;
	private final TaskFormRepo taskFormRepo;

	public UserTaskWorker(TaskRepo taskRepository, TaskFormRepo taskFormRepo) {
		this.taskRepo = taskRepository;
		this.taskFormRepo = taskFormRepo;
	}

	@Transactional
	public TaskEntity createTaskFromJob(ActivatedJob job, String name, String assignee, String candidateGroup,
			Long taskFormUuid, Long taskVariableUuid) {
		TaskEntity task = new TaskEntity();

		task.setJobKey(job.getKey());
		task.setProcessInstance(job.getProcessInstanceKey());
		task.setBpmnElementId(job.getElementId());
		task.setName(name != null ? name : job.getElementId()); // fallback
		task.setAssignee(assignee);
		task.setCandidateGroup(candidateGroup);
		task.setStatus("OPEN");
		task.setTaskFormId(taskFormUuid);
		task.setTaskVariableid(taskVariableUuid);

		return taskRepo.save(task);
	}

	@JobWorker(type = "initial-project-review-created")
	public void onInitialProjectReviewCreated(final ActivatedJob job, final JobClient client) {

		// User-task specific data (Camunda 8 user task listeners)

		Map<String, String> customHeadersMap = job.getCustomHeaders();
		TaskFormEntity taskFormEntity = new TaskFormEntity();

		if (!ObjectUtils.isEmpty(customHeadersMap.get("io.camunda.zeebe:formKey"))) {

			taskFormEntity = saveTaskForm(job, customHeadersMap.get("io.camunda.zeebe:formKey"));

		}

		TaskEntity taskEntity = createTaskFromJob(job, "initial-project-review-created", null, null,
				taskFormEntity.getId(), null);

		System.out.println("***********Done******");

		// complete the listener job so the task can actually be created
		client.newCompleteCommand(job.getKey()).send().join();
	}

	private TaskFormEntity saveTaskForm(ActivatedJob job, String formKey) {

		TaskFormEntity taskFormEntity = new TaskFormEntity();

		taskFormEntity.setFormKey(formKey);
		// for now only empty map ---// method which will check first db then call api
		taskFormEntity.setSchema(new ConcurrentHashMap<>());
		
		
		
		taskFormEntity.setVersion(null);

		return taskFormRepo.save(taskFormEntity);

	}

	public Map<Object, Object> getReviewRequestForm(String bearerToken, String formKey, long processDefinitionKey,
			int version) {

		String url = String.format(
				"https://sin-2.tasklist.camunda.io/9f6ba8a8-cba2-48c8-8fb2-fb81ccfb87de/v1/forms/%s?processDefinitionKey=%d&version=%d",
				formKey, processDefinitionKey, version);

		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("Authorization", "Bearer " + bearerToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

		return response.getBody();
	}

}
