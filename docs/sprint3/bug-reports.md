# Реєстр дефектів · ШопЛайн · Спринт 3

Реєстр у Markdown за §4.15 методички. Статус New — зафіксовано під час попереднього прогону. Поля Reviewer заповнює СМ після фактичної перевірки.

## BUG-001 · Нижній повзунок Price Range приймає 0 замість 1

**Severity:** Minor  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-003 / F-50  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Межа ціни виходить за задокументований діапазон; каталог залишається доступним.
Severity — Граничні параметри фільтра відрізняються від SRS, решта функцій працює. Priority — Інші кейси можна виконувати.

### Steps to reproduce

1. Відкрити головну сторінку Toolshop у чистому сеансі, EN.
2. Сфокусувати нижній повзунок Price Range, де поточне значення 1.
3. Натиснути ArrowLeft один раз і прочитати значення нижньої межі.

### Actual result

Нижній повзунок переходить з 1 на 0 після ArrowLeft; DOM aria-valuemin=0, aria-valuenow=0.

### Expected result

F-50 · Нижня межа 1, повзунок не приймає 0.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-001-1.jpg](attachments/BUG-001-1.jpg)

---

## BUG-002 · Неприпустиме Quantity на сторінці товару мовчки замінюється і товар додається

**Severity:** Normal  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-012, TC-013, TC-014 / GUI-30  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Користувач може покласти в кошик іншу кількість, ніж ввів; покупець має перевірити кошик вручну.
Severity — Кошик доступний, але помилкове введення дає неочікуваний стан. Priority — Інші незалежні сценарії доступні, є обхід через коректні значення.

### Steps to reproduce

1. Відкрити Toolshop як гість із порожнім кошиком.
2. Відкрити Combination Pliers.
3. Ввести 0 у Quantity і натиснути Tab.
4. Натиснути Add to cart.
5. Перевірити повідомлення і кошик. Для варіантів 100 та 1.5 повторити з порожнім кошиком.

### Actual result

Після Tab 0 → 1, 100 → 99, 1.5 → 1. Натискання Add to cart додає замінене значення.

### Expected result

GUI-30 · Показати помилку для неприпустимого значення, не додавати позицію.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-002-1.jpg](attachments/BUG-002-1.jpg), [BUG-002-2.jpg](attachments/BUG-002-2.jpg)

---

## BUG-003 · Додавання 99 одиниць двічі може створити рядок з 198 одиницями

**Severity:** Major  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-017 / F-90, BL-20  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Кошик порушує жорстку межу кількості; сума рядка зростає до $2,801.70. 198 спостерігалось у двох відтвореннях після очікування першого підтвердження. У додатковій спробі з іншим моментом натискання залишилось 99.
Severity — Важливе бізнес-обмеження кошика не виконується. Priority — Нормальні кількості та інші незалежні кейси доступні. Повне оформлення цього стану не перевірялося.

### Steps to reproduce

1. Відкрити Toolshop як гість із порожнім кошиком.
2. Відкрити Combination Pliers.
3. Ввести 99 у Quantity.
4. Натиснути Add to cart і дочекатися Product added to shopping cart.
5. Натиснути Add to cart ще раз.
6. Відкрити Cart і перевірити Quantity рядка Combination Pliers.

### Actual result

Після двох Add to cart з 99, із очікуванням першого повідомлення, Cart має один рядок Quantity 198.

### Expected result

F-90, BL-20 · Після першого додавання Quantity 99. Друге відхиляється з повідомленням, рядок лишається 99.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-003-1.jpg](attachments/BUG-003-1.jpg)

---

## BUG-004 · Загальний Total не перераховується після зміни кількості

**Severity:** Major  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-019 / F-100, BL-40  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Кошик показує дві різні суми для однієї позиції; покупець бачить помилковий підсумок до оформлення.
Severity — Порушено важливий розрахунок кошика. Priority — Інші кейси можна виконувати; фінальний інвойс не перевірявся.

### Steps to reproduce

1. Відкрити Toolshop як гість із порожнім кошиком.
2. Відкрити Combination Pliers за $14.15.
3. Ввести 2 і натиснути Add to cart.
4. Дочекатися підтвердження і повторити додавання.
5. Відкрити Cart і перевірити, що Quantity = 4, Total = $56.60.
6. Ввести 3 у Quantity цього рядка.
7. Натиснути Tab, порівняти суму рядка й загальний Total.

### Actual result

При 4 → 3 ціна $14.15, сума рядка $42.45, загальний Total $56.60 навіть через 4 с.

### Expected result

F-100, BL-40 · Обидві суми мають стати $42.45 після редагування.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-004-1.jpg](attachments/BUG-004-1.jpg)

---

## BUG-005 · У повідомленні відмови в обраному є зайва крапка

**Severity:** Trivial  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-022 / GUI-40, F-120  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Розбіжність лише з дослівно наведеним текстом SRS. Відмова гостю відображається; користувач отримує потрібну інформацію.
Severity — Косметична різниця одного символу. Priority — Жоден незалежний сценарій не заблокований.

### Steps to reproduce

1. Відкрити Toolshop у гостьовому сеансі, EN.
2. Відкрити Combination Pliers.
3. Натиснути Add to favourites.
4. Прочитати текст відмови до зникнення сповіщення.

### Actual result

Unauthorized, can not add product to your favorite list.

### Expected result

GUI-40, F-120 · Unauthorized, can not add product to your favorite list

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-005-1.jpg](attachments/BUG-005-1.jpg)

---

## BUG-006 · У шапці каталогу показано TOOLSHOP DEMO замість «ШопЛайн»

**Severity:** Trivial  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-023 / GUI-10  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Видиме брендування відрізняється від прямої вимоги SRS. Посилання логотипа має href=/; відхилення навігації не заявляється.
Severity — Косметична невідповідність назви у логотипі. Priority — Навігація та інші перевірки доступні.

### Steps to reproduce

1. Відкрити головну сторінку Toolshop у гостьовому сеансі, EN.
2. Прочитати назву на видимому логотипі у лівій частині шапки.

### Actual result

На логотипі TOOLSHOP DEMO.

### Expected result

GUI-10 · Логотип «ШопЛайн» із посиланням на головну.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-006-1.jpg](attachments/BUG-006-1.jpg)

---

## BUG-007 · Реєстрація відхиляє дату у форматі дд.мм.рррр

**Severity:** Normal  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-028 / F-10  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Користувач, який вводить дату за SRS, отримує вимогу іншого формату; повідомлення дає змогу змінити формат.
Severity — Валідний за контрактом ввід відхиляється; підказка пропонує інший формат. Priority — Інші незалежні сценарії доступні; успішна реєстрація не перевірялась.

### Steps to reproduce

1. Відкрити Toolshop у гостьовому сеансі.
2. Натиснути Sign in.
3. Натиснути Register your account.
4. Ввести 15.05.2000 у Date of Birth; інші поля лишити порожніми.
5. Натиснути Tab.
6. Натиснути Register і прочитати помилку під датою.

### Actual result

Для 15.05.2000 видно Please enter a valid date in YYYY-MM-DD format. Підказка YYYY-MM-DD.

### Expected result

F-10 · Дата 15.05.2000 проходить перевірку формату. Порожні інші поля можуть мати окремі помилки.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-007-1.jpg](attachments/BUG-007-1.jpg)

---

## BUG-008 · Обов’язкове поле Subject не має видимої зірочки

**Severity:** Minor  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-033 / U-20  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Користувач дізнається про обов’язковість теми лише після відправлення незаповненої форми.
Severity — Позначка відсутня, але текстова валідація спрацьовує. Priority — Вибір теми доступний, решта кейсів не блокуються.

### Steps to reproduce

1. Відкрити Toolshop у гостьовому сеансі.
2. Натиснути Contact.
3. Переглянути видимий підпис Subject.
4. Не заповнюючи форму, натиснути Send.
5. Прочитати валідацію поля Subject.

### Actual result

Підпис Subject без зірочки; після Send — Subject is required.

### Expected result

U-20 · Підпис обов’язкового Subject містить зірочку, помилка також передається текстом.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-008-1.jpg](attachments/BUG-008-1.jpg)

---

## BUG-009 · Картка Combination Pliers використовує AVIF замість JPEG/WebP

**Severity:** Minor  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-034 / GUI-80  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Формат зображення суперечить GUI-80. У перевіреному Chrome зображення відобразилось; наслідки для інших браузерів не виміряні.
Severity — Невідповідність формату ресурсу без спостережуваної втрати відображення. Priority — Каталог і картка відкриваються.

### Steps to reproduce

1. Відкрити головну сторінку Toolshop.
2. Знайти завантажене зображення Combination Pliers.
3. Через DevTools переглянути його src, assets/img/products/pliers01.avif.
4. Відкрити саме цей ресурс і визначити формат із сигнатури завантаженого файлу.

### Actual result

Файл pliers01.avif, 34 634 байти; контейнер має ftypavif, розмір зображення 640×432.

### Expected result

GUI-80 · Формат цього файлу JPEG або WebP. Вимірювання часу — інша перевірка.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-009-1.avif](attachments/BUG-009-1.avif)

---

## BUG-010 · Поле рядка Cart дозволяє 100 і показує суму за 100 одиниць

**Severity:** Normal  
**Priority:** P3 (пропозиція)  
**Linked test case / requirement:** TC-036 / F-100, BL-40  
**Reporter:** ШІ-помічник  
**Date:** 2026-11-12  
**Status:** New  
**Reviewer:** ___ · **Дата:** ___

### Description

Користувач бачить недопустиму кількість і розраховану за нею суму. У клієнтському інтерфейсі після Tab лишилося значення 100. Збереження на сервері не перевірене.
Severity — Стан кошика в інтерфейсі виходить за допустимі межі, кошик залишається доступним. Priority — Інші кейси доступні, валідні значення можна вводити.

### Steps to reproduce

1. Відкрити Toolshop як гість із порожнім кошиком.
2. Відкрити Combination Pliers за $14.15.
3. Натиснути Add to cart з Quantity = 1.
4. Відкрити Cart.
5. Ввести 100 у Quantity рядка Combination Pliers.
6. Натиснути Tab і прочитати кількість, суму рядка й загальний Total.

### Actual result

Після Tab Quantity = 100, сума рядка $1,415.00, загальний Total $14.15.

### Expected result

F-100, BL-40 · Не застосовувати 100; валідний стан і сума для Quantity 1 мають залишитися.

### Environment

https://practicesoftwaretesting.com · ENV-01 · 2026-11-12 · v2.5, Built 2026-10-11 · Chrome, гість, EN. Версію браузера та ОС не встановлено.

### Attachments

[BUG-010-1.jpg](attachments/BUG-010-1.jpg)
