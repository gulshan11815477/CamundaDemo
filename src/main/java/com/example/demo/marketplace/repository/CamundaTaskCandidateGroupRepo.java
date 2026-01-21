package com.example.demo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.marketplace.entity.CamundaTaskCandidateGroup;

@Repository
public interface CamundaTaskCandidateGroupRepo extends JpaRepository<CamundaTaskCandidateGroup, Long> {

    List<CamundaTaskCandidateGroup> findByGroupId(String groupId);

    // find groups where stored pipe-separated groupId contains the value
    List<CamundaTaskCandidateGroup> findByGroupIdContaining(String groupIdPart);

}
