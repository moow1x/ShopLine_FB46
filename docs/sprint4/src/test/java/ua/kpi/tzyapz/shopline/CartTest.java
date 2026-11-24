package ua.kpi.tzyapz.shopline;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ШопЛайн: бізнес-логіка кошика")
class CartTest {

    private static final String SKU = "SKU-1";
    private static final String NAME = "Combination Pliers";
    private static final BigDecimal PRICE = new BigDecimal("10.00");

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @DisplayName("BL-05, BL-10: додавання в межах 1–99 та округлення ціни HALF_UP")
    @ParameterizedTest(name = "кількість {0}, ціна {1} → {2}")
    @CsvSource({"1, 10.005, 10.01, 10.01", "2, 0.01, 0.01, 0.02",
            "98, 10.00, 10.00, 980.00", "99, 10.00, 10.00, 990.00"})
    void addItemAcceptsValidValues(int qty, String price, String storedPrice, String subtotal) {
        Cart.CartItem expected = new Cart.CartItem(SKU, NAME, new BigDecimal(storedPrice), qty);

        cart.addItem(SKU, NAME, new BigDecimal(price), qty);

        assertAll(
                () -> assertEquals(List.of(expected), List.copyOf(cart.items())),
                () -> assertEquals(new BigDecimal(subtotal), cart.subtotal())
        );
    }

    @DisplayName("BL-10: недопустиме додавання відхилено, стан кошика збережено")
    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidAddArguments")
    void addItemRejectsInvalidArguments(String scenario, String sku, BigDecimal price, int qty) {
        cart.addItem("KEEP-1", "Existing item", PRICE, 2);
        List<Cart.CartItem> before = List.copyOf(cart.items());

        assertThrows(IllegalArgumentException.class, () -> cart.addItem(sku, NAME, price, qty));

        assertEquals(before, List.copyOf(cart.items()));
    }

    private static Stream<Arguments> invalidAddArguments() {
        return Stream.of(
                Arguments.of("кількість 0", SKU, PRICE, 0),
                Arguments.of("кількість 100", SKU, PRICE, 100),
                Arguments.of("нульова ціна", SKU, BigDecimal.ZERO, 1),
                Arguments.of("від’ємна ціна", SKU, new BigDecimal("-0.01"), 1),
                Arguments.of("ціна null", SKU, null, 1),
                Arguments.of("порожній артикул", "", PRICE, 1),
                Arguments.of("артикул із пробілів", "   ", PRICE, 1),
                Arguments.of("артикул null", null, PRICE, 1)
        );
    }

    @Test
    @DisplayName("BL-20: повторне додавання одного SKU об’єднує 2 і 3 одиниці в один рядок")
    void sameSkuIncreasesQuantity() {
        cart.addItem(SKU, NAME, PRICE, 2);

        cart.addItem(SKU, NAME, PRICE, 3);

        assertAll(
                () -> assertEquals(1, cart.items().size()),
                () -> assertEquals(5, cart.qtyOf(SKU))
        );
    }

    @DisplayName("BL-20: гранична сумарна кількість одного SKU")
    @ParameterizedTest(name = "{0} + {1}: кінцева кількість {2}, очікуємо виняток={3}")
    @CsvSource({"97, 1, 98, false", "98, 1, 99, false", "98, 2, 98, true"})
    void sameSkuOverLimitIsRejected(int initialQty, int addedQty, int expectedQty, boolean rejected) {
        cart.addItem(SKU, NAME, PRICE, initialQty);
        List<Cart.CartItem> before = List.copyOf(cart.items());

        if (rejected) {
            assertThrows(IllegalArgumentException.class, () -> cart.addItem(SKU, NAME, PRICE, addedQty));
            assertEquals(before, List.copyOf(cart.items()));
        } else {
            cart.addItem(SKU, NAME, PRICE, addedQty);
            assertAll(
                    () -> assertEquals(1, cart.items().size()),
                    () -> assertEquals(expectedQty, cart.qtyOf(SKU))
            );
        }
    }
}
