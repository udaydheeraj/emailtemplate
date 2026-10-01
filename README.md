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


