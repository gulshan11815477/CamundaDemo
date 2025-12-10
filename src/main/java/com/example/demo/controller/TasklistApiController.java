package com.example.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CamundaFormDto;
import com.example.demo.model.TaskItem;
import com.example.demo.service.TasklistApiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasklist")
@RequiredArgsConstructor
public class TasklistApiController {

	private final TasklistApiClient tasklistApiClient;

	// 1. Get task by ID
	@GetMapping("/tasks/{taskId}")
	public TaskItem getTask(@PathVariable String taskId) {
		return tasklistApiClient.getTask(taskId);
	}


	// 3. Get form schema by formId
	@GetMapping("/forms/{formId}")
	public CamundaFormDto getForm(@PathVariable String formId,@RequestParam Long processDefinitionKey) {
		return tasklistApiClient.getForm(formId, processDefinitionKey);
	}
	
	
	// 4. Assign task

	@PostMapping("/tasks/{taskId}/assign")
	public void assignTask(@PathVariable String taskId, @RequestBody String userId) {
		tasklistApiClient.assignTask(taskId, userId);
		
	} 

	// 5. Unassign task
	@DeleteMapping("/tasks/{taskId}/assign")
	public void unassignTask(@PathVariable String taskId) {
		tasklistApiClient.unassignTask(taskId);
	}

	/*
	 * // 6. Complete task
	 * 
	 * @PostMapping("/tasks/{taskId}/complete") public void
	 * completeTask(@PathVariable String taskId, @RequestBody CompleteTaskRequest
	 * request) { tasklistApiClient.completeTask(taskId, request.getVariables()); }
	 */


	

	// 2. Search tasks
	/*
	 * @PostMapping("/tasks/search") public Map<String, Object>
	 * searchTasks(@RequestBody TaskSearchRequest request) { return
	 * tasklistApiClient.searchTasks(request.getQueryBody()); }
	 */
	
}