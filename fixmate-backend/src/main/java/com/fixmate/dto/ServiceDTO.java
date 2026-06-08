package com.fixmate.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceDTO {
    private Long id;
    private String name;
    private String description;
}
