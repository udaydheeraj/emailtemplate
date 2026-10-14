package com.example.emailtemplate.exception;

import java.util.Set;

public class MissingTemplateVariablesException extends RuntimeException{

    private final Set<String> missingVariables;

    public MissingTemplateVariablesException(
            Set<String> missingVariables) {

        super("Missing required template variables: "
                + String.join(", ", missingVariables));

        this.missingVariables = Set.copyOf(missingVariables);
    }

    public Set<String> getMissingVariables() {
        return missingVariables;
    }
}
