package com.example.emailtemplate.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record RenderEmailTemplateRequest(
        @NotNull(message = "Variables are required")
        @NotEmpty(message = "At least one variable is required")
        Map<String, String> variables
) {
}
