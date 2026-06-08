package com.fixmate.repository;

import com.fixmate.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByClientId(Long clientId);
    List<ServiceRequest> findByStatus(ServiceRequest.Status status);
}
