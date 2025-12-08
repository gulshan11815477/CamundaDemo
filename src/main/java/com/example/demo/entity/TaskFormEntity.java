package com.example.demo.entity;

import java.time.Instant;
import java.util.Map;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

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
@Table(name = "t_task_forms", schema = "bloom")
public class TaskFormEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_forms_seq")
	@SequenceGenerator(name = "task_forms_seq", sequenceName = "bloom.t_task_forms_new_id_seq", allocationSize = 1)
	private Long id;

// @ManyToOne
// @JoinColumn(name = "task_id", nullable = false)
// private TaskEntity task;

	private String formKey;

	@Type(JsonBinaryType.class)
	@Column(columnDefinition = "jsonb")
	private Map<String, Object> schema;

	private Integer version = 1;
	private Instant createdAt = Instant.now();

}
