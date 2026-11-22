package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ШопЛайн - валідація промокодів (BL-70)")
class PromoCodeValidatorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 22);
    private static final BigDecimal SUBTOTAL = new BigDecimal("1000.00");

    private PromoCodeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PromoCodeValidator();
    }

    @ParameterizedTest(name = "BL-70: формат коду ''{0}'' -> valid={1}")
    @CsvSource({
            "ABC12, false",
            "ABC123, true",
            "ABCD123456, true",
            "ABCD1234567, false",
            "abc123, false",
            "ABC-123, false"
    })
    void validateCodeFormatAndLength(String code, boolean expectedValid) {
        // Arrange
        PromoCode promo = new PromoCode(
                code, PromoCode.Type.PERCENT, new BigDecimal("10"), TODAY.plusDays(5), false, false);

        // Act
        PromoCodeValidator.ValidationResult result = validator.validate(promo, TODAY, SUBTOTAL);

        // Assert
        assertAll(
                () -> assertEquals(expectedValid, result.valid()),
                () -> assertEquals(expectedValid, result.reason().isEmpty())
        );
    }

    @ParameterizedTest(name = "BL-70: дата закінчення today + {0} дн. -> valid={1}")
    @CsvSource({
            "-1, false",
            "0, true",
            "1, true"
    })
    void validateExpirationDateBoundaries(long daysOffset, boolean expectedValid) {
        // Arrange
        PromoCode promo = new PromoCode(
                "SAVE10", PromoCode.Type.PERCENT, new BigDecimal("10"), TODAY.plusDays(daysOffset), false, false);

        // Act
        PromoCodeValidator.ValidationResult result = validator.validate(promo, TODAY, SUBTOTAL);

        // Assert
        assertAll(
                () -> assertEquals(expectedValid, result.valid()),
                () -> assertEquals(expectedValid, result.reason().isEmpty())
        );
    }

    @ParameterizedTest(name = "BL-70: singleUse={0}, used={1} -> valid={2}")
    @CsvSource({
            "true, false, true",
            "true, true, false",
            "false, true, true",
            "false, false, true"
    })
    void validateSingleUseFlag(boolean singleUse, boolean used, boolean expectedValid) {
        // Arrange
        PromoCode promo = new PromoCode(
                "ONCE10", PromoCode.Type.PERCENT, new BigDecimal("10"), TODAY.plusDays(5), singleUse, used);

        // Act
        PromoCodeValidator.ValidationResult result = validator.validate(promo, TODAY, SUBTOTAL);

        // Assert
        assertAll(
                () -> assertEquals(expectedValid, result.valid()),
                () -> assertEquals(expectedValid, result.reason().isEmpty())
        );
    }

    @ParameterizedTest(name = "BL-70: {0} значення={1} (subtotal=1000.00) -> valid={2}")
    @CsvSource({
            "PERCENT, 0, false",
            "PERCENT, 1, true",
            "PERCENT, 50, true",
            "PERCENT, 51, false",
            "FIXED, 0.00, false",
            "FIXED, 0.01, true",
            "FIXED, 1000.00, true",
            "FIXED, 1000.01, false"
    })
    void validatePercentAndFixedBoundaries(PromoCode.Type type, String value, boolean expectedValid) {
        // Arrange
        PromoCode promo = new PromoCode(
                "VAL123", type, new BigDecimal(value), TODAY.plusDays(5), false, false);

        // Act
        PromoCodeValidator.ValidationResult result = validator.validate(promo, TODAY, SUBTOTAL);

        // Assert
        if (expectedValid) {
            assertAll(
                    () -> assertTrue(result.valid()),
                    () -> assertTrue(result.reason().isEmpty())
            );
        } else {
            assertAll(
                    () -> assertFalse(result.valid()),
                    () -> assertFalse(result.reason().isEmpty())
            );
        }
    }
}
