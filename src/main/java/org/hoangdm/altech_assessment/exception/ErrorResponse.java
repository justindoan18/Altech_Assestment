package org.hoangdm.altech_assessment.exception;

public record ErrorResponse(
        Integer code,
        String status,
        String message
) {
}
