# 🛡️ **Web Application Firewall (WAF)**

Проект **WAF** — это веб-приложение для фильтрации запросов с использованием различных методов обнаружения аномалий, блокировки подозрительных IP-адресов и защиты от различных атак на веб-приложения. Мы применяем фильтрацию запросов на основе IP-адресов, заголовков и других метаданных запросов.

## 🚀 Стек технологий

- **Java 21**: Язык программирования для разработки.
- **Spring Boot 3.x**: Для создания и управления веб-сервисами.
- **Spring WebFlux**: Для асинхронной обработки запросов.
- **Redis**: Для временного хранения данных и блокировки IP-адресов.
- **Isolation Forest** и **Traffic Anomaly Detection**: Для выявления аномалий в трафике с использованием методов машинного обучения.
- **Netty**: Для обработки HTTP запросов.

## 🔧 Основные функции

- **Блокировка подозрительных запросов**: Обнаружение и блокировка запросов, которые могут быть частью атаки (например, с использованием подозрительных заголовков).
- **Фильтрация по IP-адресам**: Запрещает или ограничивает доступ с подозрительных IP-адресов.
- **Обнаружение аномалий в трафике**: Использование алгоритмов машинного обучения (например, Isolation Forest) для обнаружения аномальных паттернов в трафике.
- **Логирование запросов**: Все запросы записываются с подробной информацией о пути, заголовках и IP-адресах.

## 🛠️ Установка и настройка

### 📋 Требования

- Java 21
- Redis (если используется для хранения блокированных IP-адресов)
- Gradle для сборки проекта

### 🚶‍♂️ Шаги установки

1. Клонируйте репозиторий:

    ```bash
    git clone https://github.com/your-username/waf.git
    ```

2. Перейдите в директорию проекта:

    ```bash
    cd waf
    ```

3. Соберите проект с помощью Gradle:

    ```bash
    ./gradlew build
    ```

4. Запустите приложение:

    ```bash
    ./gradlew bootRun
    ```

5. Теперь приложение доступно по адресу `http://localhost:8080`.

### ⚙️ Конфигурация

- Настройки Redis можно изменить в файле `application.properties`:

    ```properties
    spring.data.redis.host=localhost
    spring.data.redis.port=6379
    ```

- Для настройки фильтров можно использовать различные параметры в `RequestFilter` и `TrafficAnomalyDetectionService`.

## 🏗️ Архитектура

### 🔑 Основные компоненты:

1. **RequestFilter**: Фильтрует входящие HTTP-запросы, блокируя подозрительные запросы и IP-адреса.
2. **IpBlockService**: Сервис для временного блокирования IP-адресов в Redis.
3. **TrafficAnomalyDetectionService**: Сервис для анализа трафика с использованием алгоритмов машинного обучения (например, Isolation Forest) для обнаружения аномалий в запросах.
4. **IsolationForest**: Модель машинного обучения для обнаружения выбросов в данных.

### 🔄 Работа приложения:

- Когда поступает запрос, он проходит через фильтр, где проверяется, не является ли он подозрительным.
- Если запрос блокируется, он не передается дальше.
- Если запрос проходит фильтрацию, он передается в дальнейшую обработку.
- В сервисе **TrafficAnomalyDetectionService** анализируется поведение трафика и определяются аномалии.
