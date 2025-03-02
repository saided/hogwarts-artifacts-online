package com.example.hogwarts_artifacts_online.artifact.dto;

import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ArtifactDto (String id,
                           @NotEmpty(message = "name is required")
                           String name,
                           @NotEmpty(message = "description is required")
                           String description,
                           @NotEmpty(message = "image is required")
                           String imageURL,
                           WizardDto owner) {
}
