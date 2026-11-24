# ШопЛайн · Автоматизація тестування, спринт 4

Проєкт команди ФБ-46 перевіряє Java-модуль ShopLine v1 за SRS 1.1 та UI-сценарії на навчальному стенді Toolshop. Робочий Maven-проєкт розташований у `docs/sprint4`.

## Запуск з чистого клона

Потрібні Git, **JDK 17** (`JAVA_HOME` має вказувати на JDK), **Maven 3.9+**, а для UI — Chrome. Під час першого запуску Maven завантажує залежності, а Selenium Manager за потреби підбирає ChromeDriver. Для UI потрібен доступ до `https://practicesoftwaretesting.com/`. Для відтворення `.side` встановіть Selenium IDE у Chrome або Firefox.

```sh
git clone https://github.com/moow1x/ShopLine_FB46.git
cd ShopLine_FB46/docs/sprint4
mvn -v

# Юніт-тести
mvn clean test

# Лише UI, без запуску юніт-тестів
mvn test "-DexcludedGroups=none" "-Dgroups=ui"

# Юніт- і UI-тести одним запуском
mvn test "-DexcludedGroups=none"

# Один UI-тест у видимому Chrome
mvn test "-DexcludedGroups=none" "-Dtest=CartUiTest" "-Dheadless=false"
```

Звіти Surefire створюються в `target/surefire-reports/`. Збережіть звіти кожного прогону перед `mvn clean` або повторним запуском того самого класу. Підсумок і зв’язки з дефектами наведено в [протоколі](docs/surefire-summary.md).

Команда з юніт-тестами повертає ненульовий код завершення, якщо тести відтворюють відомі розбіжності v1. Для Cart це приймання кількості 100 і заміна рядка з тим самим SKU замість додавання до кількості. Очікування тестів відповідають SRS. Для окремої оцінки стану браузерних сценаріїв використовуйте UI-команду. Інші падіння звіряйте з журналом, SRS та [трекером](https://github.com/moow1x/ShopLine_FB46/issues).

## Внесок учасників

| Учасник | Внесок за планом спринту |
|---|---|
| Валентин Петренко (КТ) | `CartTest` BL-10/20, `ProductTest` TC-012, `CartUiTest` TC-018, README, протокол Surefire, розділ досьє та демо. |
| Олександр Сидоров (СМ) | `CartTest` BL-40, координація code review, дошка задач і ретро. |
| Артем Марченко (ТА) | `DiscountCalculatorTest` BL-50/60, `CatalogTest` TC-009/010, матриця, UI-сценарії та реєстрація дефектів v1. |
| Єгор Якимчук (ІТ) | Maven і базовий клас UI, Selenium IDE, `PromoCodeValidatorTest` BL-70, UI TC-005/006/019 і командний прогін. |
| Євгеній Черватюк (АО) | `PromoCodeValidatorTest` BL-80, UI TC-024 та аудит відтворення з чистого клона. |

Матеріали спринту: [матриця BL](docs/coverage-matrix.md), [UI-сценарії](docs/ui-scenarios.md), [досьє](docs/automation-dossier.md), [проєкт Selenium IDE](selenium-ide/shopline.side).

## Що всередині

Пакет `ua.kpi.tzyapz.shopline`:

| Клас | Призначення | Правила SRS |
|---|---|---|
| `Cart` | Кошик покупця: `addItem(sku, name, unitPrice, qty)`, `removeItem(sku)`, `changeQty(sku, qty)`, `subtotal()`, `itemCount()`, `qtyOf(sku)`, `items()` | BL-10, BL-20, BL-40 |
| `DiscountCalculator` | Знижка за обсяг `volumeDiscountPercent(subtotal)`, `volumeDiscount(subtotal)`; вартість доставки `shippingFee(subtotalAfterDiscount)` | BL-50, BL-60 |
| `PromoCodeValidator` | Перевірка промокоду `validate(promo, today, subtotal)` і сума знижки `apply(promo, today, subtotal)` | BL-70, BL-80 |
| `PromoCode` (record) | Дані промокоду: код, тип (`PERCENT`/`FIXED`), значення, дата закінчення, ознаки одноразовості й використання | BL-70 |

Юніт-тести лежать у `src/test/java/ua/kpi/tzyapz/shopline/`, тести браузера — у підпакеті `ui`. Файли в `selenium-ide/export/` слугують довідковими чернетками, Maven їх не збирає.

## Реалізовані правила (коротко)

- **BL-10.** Кількість в одному рядку 1..99; ціна > 0; артикул непорожній; інакше `IllegalArgumentException`, кошик не змінюється.
- **BL-20.** Повторне додавання того самого артикула збільшує кількість наявного рядка (сумарно не більше 99), а не створює новий рядок.
- **BL-40.** `removeItem` і `changeQty` працюють лише з наявними рядками (`NoSuchElementException`); `changeQty` приймає 1..99; `subtotal()` — сума ціна × кількість з округленням до копійок (HALF_UP); `itemCount()` — сума кількостей.
- **BL-50.** Знижка за обсяг: ≥ 2000 грн — 5 %, ≥ 5000 грн — 10 %, ≥ 10000 грн — 15 %; лише один, найвищий рівень; округлення до копійок HALF_UP.
- **BL-60.** Доставка: 0 грн, якщо сума після знижки ≥ 1500 грн, інакше 120 грн.
- **BL-70.** Промокод: 6–10 символів A–Z0–9; дата закінчення включно; одноразовий код не використаний; `PERCENT` 1..50; `FIXED` > 0 і ≤ проміжного підсумку.
- **BL-80.** `apply` повертає суму знижки: для `PERCENT` — підсумок × відсоток / 100 (HALF_UP до копійок), для `FIXED` — суму коду; для непридатного коду — `IllegalArgumentException` із причиною.

Усі суми — `BigDecimal` у гривнях, округлення до 2 знаків за правилом `RoundingMode.HALF_UP`.
