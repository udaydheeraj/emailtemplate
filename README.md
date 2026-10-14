# emailtemplate
email-template feature : spring boot

tech stack:
Java
Spring Boot
Spring Web
Spring Data JPA
MySQL
Bean Validation
Lombok
JUnit
Mockito
Postman

---------------------------------------------------------
phase 1: project structure and architecture

com.example.emailtemplate

├── controller
│
├── service
│   └── impl
│
├── repository
│
├── entity
│
├── dto
│   ├── request
│   └── response
│
├── exception
│
├── mapper
│
└── util


--------------------------------------------------------

phase 2: database and enitity design

✅ email_template_db
✅ email_templates table
✅ EmailTemplate entity
✅ TemplateStatus enum
✅ template_code + version uniqueness
✅ ACTIVE / INACTIVE status
✅ HTML body stored as TEXT
✅ createdAt / updatedAt
✅ Versioning concept

email_templates
│
├── id
├── template_code
├── template_name
├── description
├── subject
├── html_body
├── status
├── version
├── created_at
└── updated_at

ORDER_CONFIRMATION
│
├── Version 1 ── ACTIVE
│
└── Version 2 ── INACTIVE

template_code + version
│
└── UNIQUE




