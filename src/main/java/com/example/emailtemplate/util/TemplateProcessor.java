package com.example.emailtemplate.util;

import com.example.emailtemplate.exception.InvalidTemplateVariableException;
import com.example.emailtemplate.exception.MissingTemplateVariablesException;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemplateProcessor {

    private static final Pattern PLACEHOLDER_PATTERN =
            Pattern.compile(
                    "\\{\\{\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*}}"
            );

    public Set<String> extractVariables(String... contents) {

        Set<String> variables = new LinkedHashSet<>();

        for (String content : contents) {
            if (content == null) {
                continue;
            }

            Matcher matcher =
                    PLACEHOLDER_PATTERN.matcher(content);

            while (matcher.find()) {
                variables.add(matcher.group(1));
            }
        }

        return variables;
    }

    public void validateVariables(
            Set<String> requiredVariables,
            Map<String, String> suppliedVariables) {

        Set<String> missingVariables = new LinkedHashSet<>();

        for (String variable : requiredVariables) {

            if (!suppliedVariables.containsKey(variable)
                    || suppliedVariables.get(variable) == null) {

                missingVariables.add(variable);
            }
        }

        if (!missingVariables.isEmpty()) {
            throw new MissingTemplateVariablesException(
                    missingVariables
            );
        }
    }

    public String renderSubject(
            String subject,
            Map<String, String> variables) {

        return replacePlaceholders(subject, variables, false, true);
    }

    public String renderHtml(
            String htmlBody,
            Map<String, String> variables) {

        return replacePlaceholders(htmlBody, variables, true, false);
    }

    private String replacePlaceholders(
            String template,
            Map<String, String> variables,
            boolean escapeHtml,
            boolean rejectNewlines) {

        Matcher matcher =
                PLACEHOLDER_PATTERN.matcher(template);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {

            String variableName = matcher.group(1);
            String value = variables.get(variableName);

            if (value == null) {
                throw new MissingTemplateVariablesException(
                        Set.of(variableName)
                );
            }

            if (rejectNewlines
                    && (value.contains("\r") || value.contains("\n"))) {

                throw new InvalidTemplateVariableException(
                        "Variable '" + variableName
                                + "' cannot contain newlines in the subject"
                );
            }

            String replacement = escapeHtml
                    ? escapeHtml(value)
                    : value;

            matcher.appendReplacement(
                    result,
                    Matcher.quoteReplacement(replacement)
            );
        }

        matcher.appendTail(result);

        return result.toString();
    }

    private String escapeHtml(String value) {

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}