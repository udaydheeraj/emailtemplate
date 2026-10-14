package com.example.emailtemplate.dto.response;

import com.example.emailtemplate.entity.TemplateStatus;

import java.time.LocalDateTime;

public record EmailTemplateResponse(
        Long id,

        String templateCode,

        String templateName,

        String description,

        String subject,

        String htmlBody,

        TemplateStatus status,

        Integer version,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
