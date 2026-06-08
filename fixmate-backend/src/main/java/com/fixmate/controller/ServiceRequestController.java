package com.fixmate.controller;

import com.fixmate.dto.ServiceRequestDTO;
import com.fixmate.dto.StatusUpdateDTO;
import com.fixmate.model.ServiceRequest;
import com.fixmate.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @GetMapping
    public ResponseEntity<List<ServiceRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(serviceRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRequestService.getRequestById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ServiceRequestDTO>> getRequestsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(serviceRequestService.getRequestsByClient(clientId));
    }

    @PostMapping
    public ResponseEntity<ServiceRequestDTO> createRequest(@RequestBody ServiceRequestDTO dto) {
        return ResponseEntity.ok(serviceRequestService.createRequest(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ServiceRequestDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateDTO body) {
        return ResponseEntity.ok(serviceRequestService.updateStatus(id, body.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        serviceRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
