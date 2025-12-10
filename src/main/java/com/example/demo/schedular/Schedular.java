package com.example.demo.schedular;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.model.TaskItem;
import com.example.demo.model.TaskSearchRequest;
import com.example.demo.service.TaskService;

//@Component
public class Schedular {

	@Autowired
	TaskService taskService;

	@Scheduled(fixedDelay = 3000)
	public void syncNewUserTask() {

		// fetch usertask, and save

		///
		TaskSearchRequest req = new TaskSearchRequest();
		req.setState("CREATED");

		// List<TaskItem> tasks = taskService.getTasks(req);
//
//		tasks.forEach(t -> {
//			System.out.println("Task: " + t.getId() + " - " + t.getName());
//		});

	}

}
