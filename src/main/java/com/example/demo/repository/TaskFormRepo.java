package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TaskFormEntity;

@Repository
public interface TaskFormRepo extends JpaRepository<TaskFormEntity, Long> {

	Optional<TaskFormEntity> findByFormKeyAndVersion(String formKey, int version);
}
