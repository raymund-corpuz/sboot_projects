package com.example.studentmanagement.dto;

import jakarta.validation.constraints.NotNull;

public record SetEnabledRequest(

        @NotNull(message = "enabled is required")
        Boolean enabled
) {
}
