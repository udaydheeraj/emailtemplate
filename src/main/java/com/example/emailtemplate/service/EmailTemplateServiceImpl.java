package com.example.emailtemplate.service;

import com.example.emailtemplate.dto.request.CreateEmailTemplateRequest;
import com.example.emailtemplate.dto.request.UpdateEmailTemplateRequest;
import com.example.emailtemplate.dto.response.EmailTemplateResponse;
import com.example.emailtemplate.entity.EmailTemplate;
import com.example.emailtemplate.entity.TemplateStatus;
import com.example.emailtemplate.exception.EmailTemplateAlreadyExistsException;
import com.example.emailtemplate.exception.EmailTemplateNotFoundException;
import com.example.emailtemplate.repository.EmailTemplateRepository;
import com.example.emailtemplate.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    @Override
    @Transactional
    public EmailTemplateResponse createTemplate(
            CreateEmailTemplateRequest request) {

        String code = normalizeCode(request.templateCode());

        if (emailTemplateRepository
                .findTopByTemplateCodeOrderByVersionDesc(code)
                .isPresent()) {

            throw new EmailTemplateAlreadyExistsException(
                    "Template code already exists: " + code
            );
        }

        LocalDateTime now = LocalDateTime.now();

        EmailTemplate template = new EmailTemplate();
        template.setTemplateCode(code);
        template.setTemplateName(request.templateName().trim());
        template.setDescription(normalizeDescription(request.description()));
        template.setSubject(request.subject().trim());
        template.setHtmlBody(request.htmlBody());
        template.setStatus(TemplateStatus.ACTIVE);
        template.setVersion(1);
        template.setCreatedAt(now);
        template.setUpdatedAt(now);

        return toResponse(emailTemplateRepository.save(template));
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateResponse getTemplateById(Long id) {

        EmailTemplate template = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new EmailTemplateNotFoundException(
                        "Template not found with ID: " + id
                ));

        return toResponse(template);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailTemplateResponse getActiveTemplateByCode(
            String templateCode) {

        String code = normalizeCode(templateCode);

        EmailTemplate template = emailTemplateRepository
                .findByTemplateCodeAndStatus(code, TemplateStatus.ACTIVE)
                .orElseThrow(() -> new EmailTemplateNotFoundException(
                        "No active template found for code: " + code
                ));

        return toResponse(template);
    }

    @Override
    @Transactional
    public EmailTemplateResponse updateTemplate(
            String templateCode,
            UpdateEmailTemplateRequest request) {

        String code = normalizeCode(templateCode);

        EmailTemplate latest = emailTemplateRepository
                .findTopByTemplateCodeOrderByVersionDesc(code)
                .orElseThrow(() -> new EmailTemplateNotFoundException(
                        "Template not found with code: " + code
                ));

        int nextVersion = latest.getVersion() + 1;
        LocalDateTime now = LocalDateTime.now();

        // Deactivate any currently active version for this code.
        emailTemplateRepository
                .findByTemplateCodeAndStatus(code, TemplateStatus.ACTIVE)
                .ifPresent(active -> {
                    active.setStatus(TemplateStatus.INACTIVE);
                    active.setUpdatedAt(now);
                    emailTemplateRepository.save(active);
                });

        EmailTemplate newVersion = new EmailTemplate();
        newVersion.setTemplateCode(code);
        newVersion.setTemplateName(request.templateName().trim());
        newVersion.setDescription(normalizeDescription(request.description()));
        newVersion.setSubject(request.subject().trim());
        newVersion.setHtmlBody(request.htmlBody());
        newVersion.setStatus(TemplateStatus.ACTIVE);
        newVersion.setVersion(nextVersion);
        newVersion.setCreatedAt(now);
        newVersion.setUpdatedAt(now);

        return toResponse(emailTemplateRepository.save(newVersion));
    }

    @Override
    @Transactional
    public EmailTemplateResponse activateVersion(
            String templateCode,
            Integer version) {

        String code = normalizeCode(templateCode);

        EmailTemplate target = findVersion(code, version);
        LocalDateTime now = LocalDateTime.now();

        emailTemplateRepository
                .findByTemplateCodeAndStatus(code, TemplateStatus.ACTIVE)
                .ifPresent(active -> {
                    if (!active.getId().equals(target.getId())) {
                        active.setStatus(TemplateStatus.INACTIVE);
                        active.setUpdatedAt(now);
                        emailTemplateRepository.save(active);
                    }
                });

        target.setStatus(TemplateStatus.ACTIVE);
        target.setUpdatedAt(now);

        return toResponse(emailTemplateRepository.save(target));
    }

    @Override
    @Transactional
    public EmailTemplateResponse deactivateVersion(
            String templateCode,
            Integer version) {

        String code = normalizeCode(templateCode);
        EmailTemplate target = findVersion(code, version);

        target.setStatus(TemplateStatus.INACTIVE);
        target.setUpdatedAt(LocalDateTime.now());

        return toResponse(emailTemplateRepository.save(target));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailTemplateResponse> getTemplateHistory(
            String templateCode) {

        String code = normalizeCode(templateCode);

        List<EmailTemplate> templates =
                emailTemplateRepository
                        .findByTemplateCodeOrderByVersionDesc(code);

        if (templates.isEmpty()) {
            throw new EmailTemplateNotFoundException(
                    "Template history not found for code: " + code
            );
        }

        return templates.stream()
                .map(this::toResponse)
                .toList();
    }

    private EmailTemplate findVersion(String code, Integer version) {

        return emailTemplateRepository
                .findByTemplateCodeAndVersion(code, version)
                .orElseThrow(() -> new EmailTemplateNotFoundException(
                        "Template not found for code: " + code
                                + ", version: " + version
                ));
    }

    private EmailTemplateResponse toResponse(EmailTemplate template) {

        return new EmailTemplateResponse(
                template.getId(),
                template.getTemplateCode(),
                template.getTemplateName(),
                template.getDescription(),
                template.getSubject(),
                template.getHtmlBody(),
                template.getStatus(),
                template.getVersion(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }

    private String normalizeCode(String code) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Template code must not be blank"
            );
        }

        return code.trim().toUpperCase();
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank()
                ? null
                : description.trim();
    }
}