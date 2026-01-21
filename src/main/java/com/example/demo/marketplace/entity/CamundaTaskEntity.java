package com.example.demo.marketplace.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "camunda_tasks", schema = "marketplace")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CamundaTaskEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "camunda_task_id", nullable = false, length = 64)
	private String camundaTaskId;

	@Column(name = "camunda_task_key")
	private Long camundaTaskKey;

	@Column(name = "process_instance", nullable = false)
	private Long processInstance;

	@Column(name = "process_definition_key")
	private Long processDefinitionKey;

	@Column(name = "bpmn_element_id", nullable = false, length = 50)
	private String bpmnElementId;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "assignee", length = 255)
	private String assignee;

	@Column(name = "status", nullable = false, length = 30)
	private String status;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "claimed_at")
	private LocalDateTime claimedAt;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "completed_at")
	private OffsetDateTime completedAt;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "form_id", foreignKey = @ForeignKey(name = "fk_camunda_tasks_form"))
//	private CamundaTaskFormEntity form;
	
	@Column(name = "ui_component_key", length = 50)
	private String uiComponenetForm;	

	@Column(name = "contractsummaryid")
	private Long contractSummaryId;

	@Column(name = "created_by", length = 25)
	private String createdBy;

	@Column(name = "completed_by", length = 25)
	private String completedBy;

}