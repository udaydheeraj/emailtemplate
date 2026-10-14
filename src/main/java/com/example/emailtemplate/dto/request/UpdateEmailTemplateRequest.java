package com.example.emailtemplate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateEmailTemplateRequest(
        @NotBlank(message = "Template name is required")
        @Size(max = 150, message = "Template name cannot exceed 150 characters")
        String templateName,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @NotBlank(message = "Subject is required")
        @Size(max = 255, message = "Subject cannot exceed 255 characters")
        String subject,

        @NotBlank(message = "HTML body is required")
        String htmlBody
) {
}
