package com.example.demo.service;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

@Service
public class ProcessService {

	@Autowired
	ZeebeClient zeebeClient;

	public ResponseEntity<?> deployBPMN(MultipartFile file, String name) {

		// InputStream
		// bpmInputStream=getClass().getClassLoader().getResourceAsStream(name)
		DeploymentEvent deployment;
		try {
			deployment = zeebeClient.newDeployResourceCommand().addResourceStream(file.getInputStream(), name).send()
					.join();
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deployment failed: " + e.getMessage());

		}
		return ResponseEntity.ok(deployment);
	}

	public ResponseEntity<?> startProcess(String bpmProcessId) {

		try {

			ProcessInstanceEvent processInstance = zeebeClient.newCreateInstanceCommand().bpmnProcessId(bpmProcessId)
					.latestVersion().send().join();
			return ResponseEntity.ok(processInstance);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Start failed: " + e.getMessage());

		}

	}
}

