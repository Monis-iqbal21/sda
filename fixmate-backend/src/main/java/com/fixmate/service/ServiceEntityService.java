package com.fixmate.service;

import com.fixmate.dto.ServiceDTO;
import com.fixmate.model.ServiceEntity;
import com.fixmate.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceEntityService {

    private final ServiceRepository serviceRepository;

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ServiceDTO getServiceById(Long id) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return toDTO(entity);
    }

    public ServiceDTO createService(ServiceDTO dto) {
        return toDTO(serviceRepository.save(toEntity(dto)));
    }

    public ServiceDTO updateService(Long id, ServiceDTO dto) {
        ServiceEntity entity = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return toDTO(serviceRepository.save(entity));
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    private ServiceDTO toDTO(ServiceEntity entity) {
        return ServiceDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    private ServiceEntity toEntity(ServiceDTO dto) {
        return ServiceEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}
