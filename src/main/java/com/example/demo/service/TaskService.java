package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TaskFormEntity;
import com.example.demo.entity.TaskVariableEntity;

public interface TaskService {

//	ResponseEntity<?> getAllUserTask();
//
//	public List<TaskItem> getTasks(TaskSearchRequest request);
//
//	public String getTaskHTML();

	public List<TaskEntity> searchTasks(Long taskId, String assignee, String candidateGroup);

	public Optional<TaskFormEntity> getFormById(Long taskFormId);

	public Optional<TaskVariableEntity> getVariableById(Long id);

}
