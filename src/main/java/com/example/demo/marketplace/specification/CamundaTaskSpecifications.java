package com.example.demo.marketplace.specification;

import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.marketplace.entity.CamundaTaskEntity;

public class CamundaTaskSpecifications {

    // prevent instantiation
    private CamundaTaskSpecifications() { }

    public static Specification<CamundaTaskEntity> hasCamundaTaskKey(Long camundaTaskKey) {
        return (root, query, cb) -> camundaTaskKey == null ? null : cb.equal(root.get("camundaTaskKey"), camundaTaskKey);
    }

    public static Specification<CamundaTaskEntity> hasAssignee(String assignee) {
        return (root, query, cb) -> assignee == null ? null : cb.equal(root.get("assignee"), assignee);
    }

    public static Specification<CamundaTaskEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<CamundaTaskEntity> hasIdIn(Collection<Long> ids) {
        return (root, query, cb) -> ids == null || ids.isEmpty() ? null : root.get("id").in(ids);
    }

}
