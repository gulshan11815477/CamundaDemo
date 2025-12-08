package com.example.demo.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_task_variables", schema = "bloom")
public class TaskVariableEntity {
	@Id
	@GeneratedValue
	private UUID id;

// @ManyToOne
// @JoinColumn(name = "task_id", nullable = false)
// private TaskEntity task;

	private String name;

	@Column(columnDefinition = "jsonb")
	private String value;

	private String type;
	private Instant createdAt = Instant.now();

}
