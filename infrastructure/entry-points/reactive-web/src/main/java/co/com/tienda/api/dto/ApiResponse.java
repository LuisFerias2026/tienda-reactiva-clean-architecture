package co.com.tienda.api.dto;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        String timestamp,
        int status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(OffsetDateTime.now().toString(), httpStatus.value(), message, data);
    }
}
