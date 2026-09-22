package co.com.tienda.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name must not be blank") String name,
        @NotNull(message = "price is required") @Positive(message = "price must be greater than zero") BigDecimal price,
        @NotNull(message = "stock is required") @Positive(message = "stock must be greater than zero") Integer stock) {
}
