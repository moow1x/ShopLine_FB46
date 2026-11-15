# Реєстр дефектів — Sprint 3 («ШопЛайн», demo practicesoftwaretesting.com)

Reporter: Черватюк Євгеній · Date: 15.11.2026 · Status: New · Reviewer: Петренко В.


| Bug ID | Title | Severity | Priority | Linked | Status |
|---|---|---|---|---|---|
| BUG-001 | Quantity = 0 автоматично замінюється на 1 замість повідомлення про помилку | Minor | P3 | TC-014; GUI-30 | New |
| BUG-002 | Кошик приймає сумарну кількість понад 99 при повторному додаванні (50 + 50) | Major | P3 | TC-017; F-90, BL-20 | New |
| BUG-003 | У підсумку оформлення відсутній рядок вартості доставки (Shipping) | Major | P3 | TC-020; F-110, BL-60 | New |
| BUG-004 | Вкладення у контактній формі: приймаються лише порожні файли, для решти — «File should be empty» | Major | P3 | TC-025; F-140 | New |
| BUG-005 | Відновлення пароля: замість повідомлення про успіх показано ключ «page.forgot-password.confirm» | Minor | P3 | TC-007; F-30 | New |

---
## BUG-001 — Quantity = 0 автоматично замінюється на 1 замість повідомлення про помилку
**Severity:** Minor  **Priority:** P3  **Linked:** TC-014; GUI-30 (SRS 1.1)
**Description:** SRS 1.1 допускає кількість 1–99; значення 0 має відхилятися повідомленням про допустимий діапазон. Demo мовчки підставляє 1, тому покупець не дізнається про помилку введення. Severity Minor: помилка некритична, кількість лишається допустимою. P3: інші кейси не блокує.
**Steps to reproduce:**
1. Відкрити https://practicesoftwaretesting.com у Chromium 157.0.8103.39 (нова сесія, інкогніто).
2. Натиснути картку «Combination Pliers».
3. У поле «Quantity» ввести 0 (очистивши поле).
4. Натиснути «Add to cart».
**Actual result:** Після кроку 3–4 відбувається «Product quantity update»: значення замінюється на 1; повідомлення про допустимий діапазон немає. Товар у кошик не додано.
**Expected result:** Товар не додано; біля поля повідомлення про допустимий діапазон 1–99; лічильник кошика не змінився (GUI-30).
**Environment:** demo 15.11.2026; Chromium 157.0.8103.39; Windows 11; без входу.
**Attachments:** BUG-001.png

## BUG-002 — Кошик приймає сумарну кількість понад 99 при повторному додаванні (50 + 50)
**Severity:** Major  **Priority:** P3  **Linked:** TC-017; F-90, BL-20
**Description:** За BL-20 кількість у рядку кошика не може перевищувати 99. Demo приймає друге додавання 50 без помилки, сумарно 100. Severity Major: контроль ліміту не працює, але замовлення з допустимою кількістю оформити можна. P3: кейси не блокує.
**Steps to reproduce:**
1. Відкрити https://practicesoftwaretesting.com у Chromium 157.0.8103.39 (нова сесія, інкогніто, кошик порожній).
2. Натиснути картку «Combination Pliers».
3. У поле «Quantity» ввести 50, натиснути «Add to cart».
4. У поле «Quantity» знову ввести 50, натиснути «Add to cart».
5. Натиснути піктограму кошика.
**Actual result:** Після кроку 4 помилки немає; лічильник кошика = 100.
Після кроку 5 у кошику рядок «Combination Pliers» з кількістю 100.
**Expected result:** Після кроку 4 — повідомлення про перевищення 99; лічильник лишається 50; у кошику рядок з кількістю 50.
**Environment:** demo 15.11.2026; Chromium 157.0.8103.39; Windows 11; без входу.
**Attachments:** BUG-002.png

## BUG-003 — У підсумку оформлення відсутній рядок вартості доставки (Shipping)
**Severity:** Major  **Priority:** P3  **Linked:** TC-020; F-110, BL-60
**Description:** F-110 вимагає, щоб підсумок замовлення містив вартість доставки (0.00 від порогу, інакше 120.00). У demo рядка Shipping немає на жодному кроці. Severity Major: частина обов'язкового підсумку відсутня. P3: інші кейси не блокує. Якщо КТ вирішить, що це обмеження demo, звіт можна закрити як Rejected з обґрунтуванням.
**Steps to reproduce:**
1. Відкрити https://practicesoftwaretesting.com у Chromium 157.0.8103.39 (нова сесія, інкогніто).
2. Натиснути «Sign in», увійти як customer@practicesoftwaretesting.com / welcome01.
3. Додати в кошик «Combination Pliers» (Quantity = 1).
4. Відкрити кошик, натиснути «Proceed to checkout» до кроку «Payment».
5. Переглянути блок підсумку.
**Actual result:** У підсумку немає рядка «Shipping».
**Expected result:** У підсумку є рядок «Shipping» з вартістю доставки (F-110, BL-60).
**Environment:** demo 15.11.2026; Chromium 157.0.8103.39; Windows 11; customer@practicesoftwaretesting.com.
**Attachments:** BUG-003.png

## BUG-004 — Вкладення у контактній формі: приймаються лише порожні файли, для решти — «File should be empty»
**Severity:** Major  **Priority:** P3  **Linked:** TC-025; F-140
**Description:** F-140 допускає вкладення; файл понад 100 КБ має відхилятися з повідомленням про перевищення розміру. Demo показує «File should be empty» для файлу >100 КБ, для іншого типу і для валідного .txt ≤100 КБ: приймаються лише порожні файли, тож вкладення фактично не працює, а причина відмови не видна. Severity Major: функція вкладення недоступна, але звернення без файлу надсилається. P3: кейси не блокує.
**Steps to reproduce:**
1. Відкрити https://practicesoftwaretesting.com у Chromium 157.0.8103.39 (нова сесія, інкогніто).
2. Натиснути «Contact».
3. Заповнити First name = Test; Last name = T3-Testenko; Email address = t3.contact@example.com; Subject = Customer service.
4. У поле «Message» ввести текст довжиною 50 симв.
5. У полі вкладення вибрати T3-attach-101KB.txt (103 424 байти).
6. Натиснути «Send».
**Actual result:** Повідомлення «File should be empty»; звернення не надіслано. Те саме повідомлення з'являється для звичайного .txt ≤100 КБ; приймаються лише порожні файли.
**Expected result:** Повідомлення про перевищення допустимого розміру (до 100 КБ) (F-140).
**Environment:** demo 15.11.2026; Chromium 157.0.8103.39; Windows 11; без входу.
**Attachments:** BUG-004.png

## BUG-005 — Відновлення пароля: замість повідомлення про успіх показано ключ «page.forgot-password.confirm»
**Severity:** Minor  **Priority:** P3  **Linked:** TC-007; F-30
**Description:** F-30 вимагає, щоб після надсилання форми користувач побачив повідомлення про надсилання листа. Demo показує службовий ключ локалізації «page.forgot-password.confirm», тому покупець не розуміє результат. Severity Minor: функція працює, дефект у тексті інтерфейсу. P3: кейси не блокує.
**Steps to reproduce:**
1. Відкрити https://practicesoftwaretesting.com у Chromium 157.0.8103.39 (нова сесія, інкогніто).
2. Натиснути «Sign in».
3. Натиснути посилання «Forgot your Password?».
4. У поле «Email address» ввести customer@practicesoftwaretesting.com.
5. Натиснути кнопку надсилання форми.
**Actual result:** Після кроку 5 (≈1 с) показано текст «page.forgot-password.confirm».
**Expected result:** Зрозуміле повідомлення про успішне надсилання листа для відновлення пароля (F-30).
**Environment:** demo 15.11.2026; Chromium 157.0.8103.39; Windows 11; без входу.
**Attachments:** BUG-005.png
