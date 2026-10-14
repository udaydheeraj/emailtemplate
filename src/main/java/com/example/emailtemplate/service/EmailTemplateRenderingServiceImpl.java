package com.example.emailtemplate.service;

import com.example.emailtemplate.dto.request.RenderEmailTemplateRequest;
import com.example.emailtemplate.dto.response.RenderedEmailResponse;
import com.example.emailtemplate.entity.EmailTemplate;
import com.example.emailtemplate.entity.TemplateStatus;
import com.example.emailtemplate.exception.EmailTemplateNotFoundException;
import com.example.emailtemplate.exception.InvalidTemplateVariableException;
import com.example.emailtemplate.repository.EmailTemplateRepository;
import com.example.emailtemplate.service.EmailTemplateRenderingService;
import com.example.emailtemplate.util.TemplateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailTemplateRenderingServiceImpl
        implements EmailTemplateRenderingService {

    private final EmailTemplateRepository emailTemplateRepository;
    private final TemplateProcessor templateProcessor;

    @Override
    @Transactional(readOnly = true)
    public RenderedEmailResponse render(
            String templateCode,
            RenderEmailTemplateRequest request) {

        String code = normalizeCode(templateCode);

        EmailTemplate template = emailTemplateRepository
                .findByTemplateCodeAndStatus(
                        code,
                        TemplateStatus.ACTIVE
                )
                .orElseThrow(() -> new EmailTemplateNotFoundException(
                        "No active template found for code: " + code
                ));

        Map<String, String> variables = request.variables();

        if (variables == null) {
            throw new InvalidTemplateVariableException(
                    "Template variables must not be null"
            );
        }

        Set<String> requiredVariables =
                templateProcessor.extractVariables(
                        template.getSubject(),
                        template.getHtmlBody()
                );

        templateProcessor.validateVariables(
                requiredVariables,
                variables
        );

        String renderedSubject =
                templateProcessor.renderSubject(
                        template.getSubject(),
                        variables
                );

        String renderedHtml =
                templateProcessor.renderHtml(
                        template.getHtmlBody(),
                        variables
                );

        return new RenderedEmailResponse(
                template.getTemplateCode(),
                template.getVersion(),
                renderedSubject,
                renderedHtml
        );
    }

    private String normalizeCode(String templateCode) {

        if (templateCode == null || templateCode.isBlank()) {
            throw new InvalidTemplateVariableException(
                    "Template code must not be blank"
            );
        }

        return templateCode.trim().toUpperCase();
    }
}