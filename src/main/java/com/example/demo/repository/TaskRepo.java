package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TaskEntity;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
}