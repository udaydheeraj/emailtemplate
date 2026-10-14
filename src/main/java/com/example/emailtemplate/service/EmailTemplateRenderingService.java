package com.example.emailtemplate.service;

import com.example.emailtemplate.dto.request.RenderEmailTemplateRequest;
import com.example.emailtemplate.dto.response.RenderedEmailResponse;
import org.springframework.stereotype.Service;


public interface EmailTemplateRenderingService {
    RenderedEmailResponse render(
            String templateCode,
            RenderEmailTemplateRequest request
    );
}
