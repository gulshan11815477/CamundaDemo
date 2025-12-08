package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ProcessService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;


@RestController
public class ProcessController {
//
	@Autowired
	private ProcessService processService;
//
//	@PostMapping("/deployee")
//	ResponseEntity<?> deployBPMN(@RequestParam("file") MultipartFile file) {
//		String fileName = file.getOriginalFilename();
//
//		return processService.deployBPMN(file, fileName);
//
//	}
//
//	@GetMapping("/getAllRunningProcess")
//	ResponseEntity<?> allRunningProcess(@RequestParam String bpmProcessId) {
//
//		return processService.startProcess(bpmProcessId);
//
//	}
//
//	// create instance of bpmn
//
	@GetMapping("/startProcess")
	ResponseEntity<?> startProcess(@RequestParam String bpmProcessId) {

		return processService.startProcess(bpmProcessId);

	}
	
//	
//	@JobWorker(type = "hello",autoComplete = true)	
//	public void proceessdata(final JobClient jobClient,final ActivatedJob activatedJob ) {
//		
//		Map<String, Object>variablesMap=activatedJob.getVariablesAsMap();
//		System.out.println("payload of camunda"+variablesMap );
//		
//		variablesMap.put("key1",9632145 );
//		variablesMap.put("isOk",false );
//		
//		// now we want to send
//			
//		jobClient.newCompleteCommand(activatedJob.getKey()).variables(variablesMap).send();
//		
//		
//	}
//
//	
//	
}
