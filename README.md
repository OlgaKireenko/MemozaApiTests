# 📌 Инструкция по запуску тестов и генерации отчёта

## 1. Клонирование репозитория

```bash
git clone https://github.com/OlgaKireenko/MemozaApiTests
cd <папка_проекта>
```

---

## 2. Запуск тестов на разных окружениях

В проекте используется класс `memoza.data.Config`, который подгружает настройки из файлов:

```
application-{env}.properties
```

где `{env}` — название окружения.

### Доступные окружения

* `demo` — окружение по умолчанию (если параметр `-Denv` не указан)
* `scdev05` - песочница


Конфигурационные файлы находятся в:

```
src/test/resources/
```

### Примеры запуска

```bash
./gradlew clean test -Denv=scdev05

```
Если не указывать `-Denv`, будет использоваться `application-demo.properties`.

---

## 3. Генерация Allure-отчёта

После выполнения тестов можно сгенерировать HTML-отчёт:

```bash
./gradlew allureReport
```

Отчёт будет находиться по пути:

```
build/reports/allure-report/index.html
```

Чтобы открыть отчёт, откройте файл `index.html` в браузере.

---

## 4. Запуск с генерацией и открытием Allure-отчёта

```bash
./gradlew clean test allureReport allureServe
```

Команда `allureServe` автоматически запустит локальный сервер и откроет отчёт в браузере.

---

## 5. CI/CD //TO DO:

В проекте есть GitHub Actions для автоматического запуска тестов и публикации Allure-отчёта в GitHub Pages при пуше в ветку `master`. После прогона тестов отчёт доступен тут => https://olgakireenko.github.io/MemozaApiTests/allureReport/
