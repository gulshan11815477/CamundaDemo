package com.example.demo.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "demo_t_tasks", schema = "bloom")
public class TaskEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_seq")
	@SequenceGenerator(name = "tasks_seq", sequenceName = "bloom.t_tasks_id_seq",
			allocationSize = 1)

	private long id;

	@Column(name = "job_key", nullable = false)
	private Long jobKey;

	@Column(name = "process_instance", nullable = false)
	private Long processInstance;

	@Column(name = "bpmn_element_id", nullable = false)
	private String bpmnElementId;

	private String name;
	private String assignee;
	private String candidateGroup;

	@Column(nullable = false)
	private String status; // OPEN, COMPLETED, CANCELLED

	private Instant createdAt = Instant.now();
	private Instant completedAt;

	@Column(name = "table_task_form_id")
	private Long taskFormId;

	@Column(name = "table_task_variable_id")
	private Long taskVariableid;
	
	@Column(name = "user_task_id")
	private String userTaskId;
	
	@Column(name = "process_definiation_key")
	private Long processDefinationKey;
	
	

	/*
	 * //@ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "task_form_id", nullable = false) private TaskFormEntity
	 * taskForm;
	 * 
	 * // @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "task_variable_id", nullable = false) private
	 * TaskVariableEntity taskVariable;
	 */

}