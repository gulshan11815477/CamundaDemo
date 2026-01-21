package com.example.demo.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.marketplace.entity.CamundaTaskEntity;

@Repository
public interface CamundaTaskRepository extends JpaRepository<CamundaTaskEntity, Long>, JpaSpecificationExecutor<CamundaTaskEntity> {

    Optional<CamundaTaskEntity> findByCamundaTaskKey(Long camundaTaskKey);

    Optional<CamundaTaskEntity> findByCamundaTaskKeyAndStatus(Long camundaTaskKey,String status);

    @Query("SELECT f.uiComponenetForm FROM CamundaTaskEntity f WHERE f.camundaTaskKey =:camundaTaskKey")
    String getUiComponentFormByCamundaTaskKey(@Param("camundaTaskKey") Long camundaTaskKey);

    Optional<CamundaTaskEntity> findByCamundaTaskId(String camundaTaskId);

    // atomic update of assignee and status by camunda task id
    @Modifying
    @Transactional
    @Query("UPDATE CamundaTaskEntity t SET t.assignee = :assignee, t.status = :status, t.claimedAt = current_timestamp WHERE t.camundaTaskId = :camundaTaskId")
    int updateAssigneeAndStatusByCamundaTaskId(@Param("camundaTaskId") String camundaTaskId, @Param("assignee") String assignee, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE CamundaTaskEntity t SET t.completedBy = :assignee, t.status = :completed, t.completedAt = current_timestamp WHERE t.camundaTaskId = :taskId")
    int completeTask(String taskId, String assignee, String completed);
}
