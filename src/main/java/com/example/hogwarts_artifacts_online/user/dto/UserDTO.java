package com.example.hogwarts_artifacts_online.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDTO  (Long id,

                        @NotEmpty(message = "username is required")
                        String username,

                        @NotEmpty(message = "roles are required")
                        String roles,
                        boolean enabled){}