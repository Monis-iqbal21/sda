package com.fixmate.dto;

import com.fixmate.model.ServiceRequest;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StatusUpdateDTO {
    private ServiceRequest.Status status;
}
