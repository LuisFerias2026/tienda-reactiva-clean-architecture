package co.com.tienda.api.validator;

import co.com.tienda.api.dto.ProductRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

class RequestValidatorTest {
    private RequestValidator requestValidator;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        requestValidator = new RequestValidator(validator);
    }

    @Test
    void shouldEmitDtoWhenValid() {
        ProductRequest request = new ProductRequest("Cafe", new BigDecimal("12500.00"), 10);

        StepVerifier.create(requestValidator.validate(request))
                .expectNext(request)
                .verifyComplete();
    }

    @Test
    void shouldEmitConstraintViolationWhenInvalid() {
        ProductRequest request = new ProductRequest("", new BigDecimal("-1"), 0);

        StepVerifier.create(requestValidator.validate(request))
                .expectError(ConstraintViolationException.class)
                .verify();
    }

    @Test
    void shouldEmitIllegalArgumentWhenBodyIsNull() {
        StepVerifier.create(requestValidator.validate(null))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
