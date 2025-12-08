package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.TaskEntity;

public class TaskSpecifications {
	public static Specification<TaskEntity> hasTaskId(Long taskId) {
		return (root, query, cb) -> taskId == null ? null : cb.equal(root.get("id"), taskId);
	}

	public static Specification<TaskEntity> hasAssignee(String assignee) {
		return (root, query, cb) -> assignee == null ? null : cb.equal(root.get("assignee"), assignee);
	}

	public static Specification<TaskEntity> hasCandidateGroup(String candidateGroup) {
		return (root, query, cb) -> candidateGroup == null ? null
				: cb.equal(root.get("candidateGroup"), candidateGroup);
	}

}
