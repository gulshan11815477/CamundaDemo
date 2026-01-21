package com.example.demo.marketplace.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.marketplace.entity.CamundaTaskCandidateGroup;
import com.example.demo.marketplace.entity.CamundaTaskEntity;
import com.example.demo.marketplace.repository.CamundaTaskCandidateGroupRepo;
import com.example.demo.marketplace.repository.CamundaTaskFormRepository;
import com.example.demo.marketplace.repository.CamundaTaskRepository;
import com.example.demo.marketplace.specification.CamundaTaskSpecifications;

@Service
public class MarketplaceTaskServiceImpl implements MarketplaceTaskService {

    private final CamundaTaskRepository taskRepo;
    private final CamundaTaskFormRepository formRepo;
    private final CamundaTaskCandidateGroupRepo candidateGroupRepo;

    public MarketplaceTaskServiceImpl(CamundaTaskRepository taskRepo, CamundaTaskFormRepository formRepo,
            CamundaTaskCandidateGroupRepo candidateGroupRepo) {
        this.taskRepo = taskRepo;
        this.formRepo = formRepo;
        this.candidateGroupRepo = candidateGroupRepo;
    }

    @Override
    public List<CamundaTaskEntity> searchTasks(Long camundaTaskKey, String assignee, String status) {
        Specification<CamundaTaskEntity> spec = Specification.where(CamundaTaskSpecifications.hasCamundaTaskKey(camundaTaskKey))
                .and(CamundaTaskSpecifications.hasAssignee(assignee))
                .and(CamundaTaskSpecifications.hasStatus(status));

        return taskRepo.findAll(spec);
    }

    @Override
    public List<CamundaTaskEntity> searchTasksByCandidateGroup(String candidateGroupId, String status) {
        if (candidateGroupId == null || candidateGroupId.isBlank()) {
            return List.of();
        }

        List<CamundaTaskCandidateGroup> groups = candidateGroupRepo.findByGroupIdContaining(candidateGroupId);
        if (groups == null || groups.isEmpty()) {
            return List.of();
        }

        Collection<Long> taskIds = groups.stream().map(g -> g.getTaskId().getId()).collect(Collectors.toSet());
        if (taskIds.isEmpty()) {
            return List.of();
        }

        Specification<CamundaTaskEntity> spec = Specification.where(CamundaTaskSpecifications.hasIdIn(taskIds))
                .and(CamundaTaskSpecifications.hasStatus(status));

        return taskRepo.findAll(spec);
    }

    @Override
    public Optional<CamundaTaskEntity> findByCamundaTaskKey(Long camundaTaskKey) {
        return taskRepo.findByCamundaTaskKey(camundaTaskKey);
    }

    @Override
    public String getUiFormId(Long camundaTaskKey) {
        return taskRepo.getUiComponentFormByCamundaTaskKey(camundaTaskKey);

    }

//    @Override
//    public Optional<CamundaTaskFormEntity> getFormById(Long formId) {
//        return formRepo.findById(formId);
//    }

    @Override
    public CamundaTaskEntity save(CamundaTaskEntity task) {
        return taskRepo.save(task);
    }

    @Override
    @Transactional
    public void assignTask(Long camundaTaskKey, String assignee) {
        if (camundaTaskKey == null || assignee == null || assignee.isBlank()) {
            return;
        }
        Optional<CamundaTaskEntity> opt = taskRepo.findByCamundaTaskKey(camundaTaskKey);
        if (opt.isPresent()) {
            CamundaTaskEntity task = opt.get();
            // idempotent: if already assigned to same user, nothing to do
            if (Objects.equals(assignee, task.getAssignee())) {
                return;
            }
            task.setAssignee(assignee);
            task.setStatus("CLAIMED");
            task.setClaimedAt(LocalDateTime.now());
            taskRepo.save(task);
        }
    }

    @Override
    @Transactional
    public void completeTask(Long camundaTaskKey, String completedBy) {
        if (camundaTaskKey == null) {
            return;
        }
        Optional<CamundaTaskEntity> opt = taskRepo.findByCamundaTaskKeyAndStatus(camundaTaskKey, "CLAIMED");
        if (opt.isPresent()) {
            CamundaTaskEntity task = opt.get();
            task.setStatus("COMPLETED");
            task.setCompletedBy(completedBy);
            task.setCompletedAt(java.time.OffsetDateTime.now());
            taskRepo.save(task);
        }
    }
}
