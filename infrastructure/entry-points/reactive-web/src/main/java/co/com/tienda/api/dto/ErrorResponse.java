package co.com.tienda.api.dto;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String path,
        Object message
) {
    public static ErrorResponse of(HttpStatus httpStatus, String path, Object message) {
        return new ErrorResponse(
                OffsetDateTime.now().toString(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                path,
                message
        );
    }
}
