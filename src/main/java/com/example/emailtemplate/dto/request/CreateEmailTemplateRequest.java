package com.example.emailtemplate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEmailTemplateRequest(

        @NotBlank(message = "Template code is required")
        @Size(max = 100, message = "Template code cannot exceed 100 char")
        String templateCode,

        @NotBlank(message = "Template Name is required")
        @Size(max = 150, message = "template Name cannot exceed 150 char")
        String templateName,

        @Size(max = 500, message = "Description cannot exceed 500 char")
        String description,

        @NotBlank(message = "subject is required")
        @Size(max = 255, message = "Subject cannot exceed 255 char")
        String subject,

        @NotBlank(message = "Html body required")
        String htmlBody


){

}
