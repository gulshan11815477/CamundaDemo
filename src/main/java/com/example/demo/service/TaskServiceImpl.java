package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskFormEntity;
import com.example.demo.entity.TaskVariableEntity;
import com.example.demo.repository.TaskFormRepo;
import com.example.demo.repository.TaskRepo;
import com.example.demo.repository.TaskVariableRepo;
import com.example.demo.specification.TaskSpecifications;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepo taskRepo;

	@Autowired
	TaskFormRepo taskFormRepo;
	@Autowired
	TaskVariableRepo taskVariableRepo;

//	@Autowired
//	WebClient webClient;
//
//	@Override
//	public ResponseEntity<?> getAllUserTask() {
//
//		String response = WebClient.create("http://localhost:8080").post().uri("/api/tasks")
////			    .bodyValue(new Task("123", "Demo Task")) // request body
//				.retrieve().bodyToMono(String.class).block();
//
//		return ResponseEntity.ok(response);
//	}
//	

	/*
	 * @Override public List<TaskItem> getTasks(TaskSearchRequest request) {
	 * 
	 * String response = tasklistWebClient.get().uri("/v1/tasks") // ← Tasklist REST
	 * endpoint .headers(headers ->
	 * headers.setBearerAuth(CamundaTokenFetcher.fetchAccessTokenString())) //
	 * .bodyValue(request) .retrieve().bodyToMono(String.class).block();
	 * 
	 * 
	 * return Collections.EMPTY_LIST; }
	 * 
	 * public String getTaskHTML() {
	 * 
	 * String response = tasklistWebClient.get().uri("/v1/tasks/search") // ←
	 * Tasklist REST endpoint .headers(headers ->
	 * headers.setBearerAuth(CamundaTokenFetcher.fetchAccessTokenString()))
	 * 
	 * .retrieve().bodyToMono(String.class).block();
	 * 
	 * 
	 * }
	 */

	@Override
	public List<TaskEntity> searchTasks(Long taskId, String assignee, String candidateGroup) {
		Specification<TaskEntity> spec = Specification.where(TaskSpecifications.hasTaskId(taskId))
				.and(TaskSpecifications.hasAssignee(assignee))
				.and(TaskSpecifications.hasCandidateGroup(candidateGroup));

		return taskRepo.findAll(spec);
	}

	@Override
	public Optional<TaskFormEntity> getFormById(Long taskFormId) {
		return taskFormRepo.findById(taskFormId);
	}

	@Override
	public Optional<TaskVariableEntity> getVariableById(Long id) {
		return taskVariableRepo.findById(id);
	}

}
