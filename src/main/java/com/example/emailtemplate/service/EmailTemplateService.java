package com.example.emailtemplate.service;

import com.example.emailtemplate.dto.request.CreateEmailTemplateRequest;
import com.example.emailtemplate.dto.request.UpdateEmailTemplateRequest;
import com.example.emailtemplate.dto.response.EmailTemplateResponse;

import java.util.List;

public interface EmailTemplateService {

    EmailTemplateResponse createTemplate(CreateEmailTemplateRequest request);

    EmailTemplateResponse getTemplateById(Long id);

    EmailTemplateResponse getActiveTemplateByCode(String templateCode);

    EmailTemplateResponse updateTemplate(
            String templateCode,
            UpdateEmailTemplateRequest request
    );

    EmailTemplateResponse activateVersion(
            String templateCode,
            Integer version
    );

    EmailTemplateResponse deactivateVersion(
            String templateCode,
            Integer version
    );

    List<EmailTemplateResponse> getTemplateHistory(
            String templateCode
    );
}
