package com.example.demo.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskFormEntity;
import com.example.demo.model.CamundaFormDto;
import com.example.demo.repository.TaskFormRepo;
import com.example.demo.repository.TaskRepo;
import com.example.demo.service.TasklistApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class UserTaskWorker {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	TasklistApiClient tasklistApiClient;

	private final TaskRepo taskRepo;
	private final TaskFormRepo taskFormRepo;

	public UserTaskWorker(TaskRepo taskRepository, TaskFormRepo taskFormRepo) {
		this.taskRepo = taskRepository;
		this.taskFormRepo = taskFormRepo;
	}

	private static final ObjectMapper mapper = new ObjectMapper();

	@Transactional
	public TaskEntity createTaskFromJob(ActivatedJob job, String name, String assignee, String candidateGroup,
			Long taskFormUuid, Long taskVariableUuid) {
		TaskEntity task = new TaskEntity();

		task.setJobKey(job.getKey());
		task.setUserTaskId(job.getCustomHeaders().get("io.camunda.zeebe:userTaskKey"));
		task.setProcessDefinationKey(job.getProcessDefinitionKey());
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

		if (!ObjectUtils.isEmpty(job.getCustomHeaders().get("io.camunda.zeebe:formKey"))) {

			taskFormEntity = saveTaskForm(job.getProcessDefinitionKey(),
					customHeadersMap.get("io.camunda.zeebe:formKey"));

		}

		TaskEntity taskEntity = createTaskFromJob(job, "initial-project-review-created", null, null,
				taskFormEntity.getId(), null);

		System.out.println("***********Done******");

		// complete the listener job so the task can actually be created
		client.newCompleteCommand(job.getKey()).send().join();
	}

	public TaskFormEntity saveTaskForm(long processDefinitionkey, String formKey) {

		TaskFormEntity taskFormEntity = new TaskFormEntity();

		taskFormEntity.setFormKey(formKey);
		CamundaFormDto camundaFormDto = tasklistApiClient.getForm(formKey, processDefinitionkey);
		taskFormEntity.setVersion(camundaFormDto.getVersion());

//		taskFormEntity.setSchema(new ConcurrentHashMap<>());

		Map<String, Object> stringMap = null;
		try {
			stringMap = mapper.readValue(camundaFormDto.getSchema().toString(), Map.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		taskFormEntity.setSchema(Optional.ofNullable(stringMap).orElseGet(HashMap::new));

		return taskFormRepo.save(taskFormEntity);

	}

//
//	/**
//	 * Converts a JsonNode to a Map<String, Object>.
//	 *
//	 * @param node the JsonNode to convert
//	 * @return a Map representation of the JsonNode
//	 */
//	public static Map<String, Object> convertJsonNodeToMap(JsonNode node) {
//		return mapper.convertValue(node, Map.class);
//	}

	/*
	 * public Map<Object, Object> getReviewRequestForm(String bearerToken, String
	 * formKey, long processDefinitionKey, int version) {
	 * 
	 * String url = String.format(
	 * "https://sin-2.tasklist.camunda.io/9f6ba8a8-cba2-48c8-8fb2-fb81ccfb87de/v1/forms/%s?processDefinitionKey=%d&version=%d",
	 * formKey, processDefinitionKey, version);
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.set("accept",
	 * "application/json"); headers.set("Authorization", "Bearer " + bearerToken);
	 * 
	 * HttpEntity<String> entity = new HttpEntity<>(headers);
	 * 
	 * ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET,
	 * entity, Map.class);
	 * 
	 * return response.getBody(); }
	 * 
	 */
}
