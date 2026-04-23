package com.taskflow.taskflow_api.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class AuthRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private String fullName;
}
