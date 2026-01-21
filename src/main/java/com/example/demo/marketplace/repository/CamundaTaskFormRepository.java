package com.example.demo.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.marketplace.entity.CamundaTaskFormEntity;

@Repository
public interface CamundaTaskFormRepository extends JpaRepository<CamundaTaskFormEntity, Long> {

//    Optional<CamundaTaskFormEntity> findById(Long id);

}
