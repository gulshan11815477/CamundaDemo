package com.example.demo.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.config.CamundaAuthService;
import com.example.demo.model.CamundaFormDto;
import com.example.demo.model.TaskItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TasklistApiClient {

	private final WebClient camundaClient;
	private final CamundaAuthService authService;

	private String bearer() {
		return "Bearer " + authService.getToken();
	}

	// 1. Get task by ID
	public TaskItem getTask(String taskId) {
		String response = camundaClient.get().uri("/tasks/{id}", taskId).header(HttpHeaders.AUTHORIZATION, bearer())
				.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();

		// If you get HTML → token invalid OR Accept header missing
		if (response != null && response.startsWith("<")) {
			throw new RuntimeException("Camunda Tasklist returned HTML instead of JSON. "
					+ "Check authentication token or Accept header. Response: " + response);
		}

		// Convert JSON string to TaskItem
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(response, TaskItem.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse Tasklist JSON into TaskItem: " + response, e);
		}

	}

	// 2. Search tasks
	public Map<String, Object> searchTasks(Map<String, Object> queryBody) {
		return camundaClient.post().uri("/tasks/search").header(HttpHeaders.AUTHORIZATION, bearer())
				.bodyValue(queryBody).retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).block();
	}

	// 3. Get form schema by formId
	public CamundaFormDto getForm(String formId, Long processDefinitionKey) {

		return camundaClient.get()
				.uri(uriBuilder -> uriBuilder.path("/forms/{id}")
						.queryParam("processDefinitionKey", processDefinitionKey).build(formId))
				.header(HttpHeaders.AUTHORIZATION, bearer()).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(CamundaFormDto.class).block();

	}

	// 4. Assign task
	public void assignTask(String taskId, String userId) {
		camundaClient.patch() .uri("/tasks/{id}/assignee", taskId).header(HttpHeaders.AUTHORIZATION, bearer())
				.bodyValue(Map.of("assignee", userId)).retrieve().toBodilessEntity().block();
	}

	// 5. Unassign task
	public void unassignTask(String taskId) {
		camundaClient.delete().uri("/tasks/{id}/assignee", taskId).header(HttpHeaders.AUTHORIZATION, bearer())
				.retrieve().toBodilessEntity().block();
	}

	// 6. Complete task
	public void completeTask(String taskId, Map<String, Object> variables) {
		camundaClient.post().uri("/tasks/{id}/complete", taskId).header(HttpHeaders.AUTHORIZATION, bearer())
				.bodyValue(Map.of("variables", variables)).retrieve().toBodilessEntity().block();
	}
}
