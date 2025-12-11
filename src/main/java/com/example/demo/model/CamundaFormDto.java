package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CamundaFormDto {

	private String id;
	private long processDefinitionKey;
	private Object schema;
	private int version;
	private String tenantId;
	private boolean isDeleted;
}