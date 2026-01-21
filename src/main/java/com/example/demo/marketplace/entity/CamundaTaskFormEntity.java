package com.example.demo.marketplace.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "t_task_forms", schema = "marketplace")
public class CamundaTaskFormEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "form_key", length = 255)
	private String formKey;

	@Column(name = "ui_component_key", length = 50)
	private String uiComponentKey;

	@Column(name = "version")
	private Integer version = 1; // default value

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now(); // default timestamp
}