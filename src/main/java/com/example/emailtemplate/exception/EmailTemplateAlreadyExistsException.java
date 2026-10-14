package com.example.emailtemplate.exception;

public class EmailTemplateAlreadyExistsException extends RuntimeException{
   public EmailTemplateAlreadyExistsException(String message)
    {
        super(message);
    }
}
