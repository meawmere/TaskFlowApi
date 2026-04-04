## Стек

Java 17, Spring Boot 4 (Web, Security, Data JPA, Validation), PostgreSQL, jjwt, Lombok, Maven. Сборка через `./mvnw` или системный Maven.

## Что внутри

Spring Security в stateless-режиме, JWT в фильтре, пароли через BCrypt. Ответы через общую обёртку, ошибки и валидация — централизованно. Задачи в БД через JPA, доступ только у владельца. Docker Compose поднимает приложение и Postgres; в репозитории есть multi-stage Dockerfile.

## Запуск

**Docker:** скопируй `.env.example` в `.env`, задай значения (для JWT нужен длинный секрет), потом `docker compose up --build`. Сервис слушает порт из `docker-compose.yaml`, все методы API под путём `/api`.

**Локально:** своя PostgreSQL, переменные окружения `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS` — и `./mvnw spring-boot:run`. JAR: `./mvnw package` и `java -jar target/TaskFlow-0.0.1-SNAPSHOT.jar`.

Для compose дополнительно используются `DB_NAME`, `DB_USER`, `DB_PASSWORD` из `.env`. Сам `.env` в git не входит.

## API

Нужен JWT в заголовке `Authorization: Bearer …` для всего под `/api/tasks`.

| Метод | Что делает |
|-------|------------|
| `POST /api/auth/signup` | регистрация → 201 |
| `POST /api/auth/signin` | вход, токен в `data` |
| `GET`, `POST /api/tasks` | список, создание (201 + `Location`) |
| `GET`, `PATCH`, `DELETE /api/tasks/{id}` | одна задача; DELETE → 204 |

## Лицензия

[MIT](LICENSE) — делай с кодом что угодно, оставь копирайт и текст лицензии в копиях.
