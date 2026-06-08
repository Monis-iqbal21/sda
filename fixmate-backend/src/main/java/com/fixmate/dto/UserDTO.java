package com.fixmate.dto;

import com.fixmate.model.User;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private User.Role role;
}
