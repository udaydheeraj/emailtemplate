package com.example.emailtemplate.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "email_templates"
)
@Getter
@Setter
@NoArgsConstructor
public class EmailTemplate {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;


    @Column(
            name = "template_code",
            nullable = false,
            length = 100
    )
    private String templateCode;


    @Column(
            name = "template_name",
            nullable = false,
            length = 150
    )
    private String templateName;


    @Column(
            length = 500
    )
    private String description;


    @Column(
            nullable = false,
            length = 255
    )
    private String subject;


    @Column(
            name = "html_body",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String htmlBody;


    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private TemplateStatus status;

    @Column(nullable = false)
    private Integer version;


    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;


    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

}
