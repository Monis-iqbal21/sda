package com.fixmate.repository;

import com.fixmate.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByWorkerId(Long workerId);
    List<Assignment> findByServiceRequestId(Long serviceRequestId);
}
