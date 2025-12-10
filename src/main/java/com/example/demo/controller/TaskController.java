package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskFormEntity;
import com.example.demo.entity.TaskVariableEntity;
import com.example.demo.service.TaskService;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;

	@GetMapping("/getTasks")
	public List<TaskEntity> getTasks(@RequestParam(required = false) Long taskId,
			@RequestParam(required = false) String assignee, @RequestParam(required = false) String candidateGroup) {

		return taskService.searchTasks(taskId, assignee, candidateGroup);
	}

	@GetMapping("/getTaskForm")
	public Optional<TaskFormEntity> getTaskForm(@RequestParam Long taskFormId) {
		return taskService.getFormById(taskFormId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskVariableEntity> getTaskVariable(@PathVariable Long id) {
		return taskService.getVariableById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	
	
	
}
