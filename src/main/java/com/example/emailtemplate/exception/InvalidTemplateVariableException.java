package com.example.emailtemplate.exception;

public class InvalidTemplateVariableException extends RuntimeException{
    public InvalidTemplateVariableException(String message) {
        super(message);
    }
}
