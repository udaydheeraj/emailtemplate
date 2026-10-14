package com.example.emailtemplate.dto.response;

public record RenderedEmailResponse(
        String templateCode,
        Integer version,
        String subject,
        String htmlBody
) {
}
