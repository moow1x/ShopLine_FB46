package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ШопЛайн - знижки та доставка (BL-50, BL-60)")
class DiscountCalculatorTest {

    private DiscountCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DiscountCalculator();
    }

    @ParameterizedTest(name = "BL-50: {0} грн -> {1}%")
    @CsvSource({
            "1999.99, 0",
            "2000.00, 5",
            "2000.01, 5",
            "4999.99, 5",
            "5000.00, 10",
            "5000.01, 10",
            "9999.99, 10",
            "10000.00, 15",
            "10000.01, 15"
    })
    void volumeDiscountPercentAtBoundaries(String subtotal, int expectedPercent) {
        // Arrange
        BigDecimal amount = new BigDecimal(subtotal);

        // Act
        int actualPercent = calculator.volumeDiscountPercent(amount);

        // Assert
        assertEquals(expectedPercent, actualPercent);
    }

    @ParameterizedTest(name = "BL-50: знижка для {0} грн = {1} грн")
    @CsvSource({
            "2333.33, 116.67",
            "10000.00, 1500.00"
    })
    void volumeDiscountUsesHighestTierAndHalfUp(String subtotal, String expectedDiscount) {
        // Arrange
        BigDecimal amount = new BigDecimal(subtotal);
        BigDecimal expected = new BigDecimal(expectedDiscount);

        // Act
        BigDecimal actualDiscount = calculator.volumeDiscount(amount);

        // Assert
        assertEquals(expected, actualDiscount);
    }

    @ParameterizedTest(name = "BL-60: доставка для {0} грн = {1} грн")
    @CsvSource({
            "1499.99, 120.00",
            "1500.00, 0.00",
            "1500.01, 0.00"
    })
    void shippingFeeAtThreshold(String subtotalAfterDiscount, String expectedFee) {
        // Arrange
        BigDecimal amount = new BigDecimal(subtotalAfterDiscount);
        BigDecimal expected = new BigDecimal(expectedFee);

        // Act
        BigDecimal actualFee = calculator.shippingFee(amount);

        // Assert
        assertEquals(expected, actualFee);
    }

    @Test
    @DisplayName("BL-50, BL-60: від'ємні суми викликають IllegalArgumentException")
    void calculatorRejectsInvalidSubtotal() {
        // Arrange
        BigDecimal negativeSubtotal = new BigDecimal("-0.01");
        BigDecimal negativeShippingAmount = new BigDecimal("-1.00");

        // Act + Assert
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> calculator.volumeDiscountPercent(negativeSubtotal)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> calculator.volumeDiscount(negativeSubtotal)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> calculator.shippingFee(negativeShippingAmount))
        );
    }
}
