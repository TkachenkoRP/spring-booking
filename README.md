# Сервис бронирования отелей

Бэкенд-составляющая сервиса бронирования отелей с возможностью управлять контентом через административную панель CMS

## Используемые технологии

* REST API приложение, разработанное с использованием Spring MVC.
* Валидация входящих запросов.
* AOP (Аспектно-ориентированное программирование).
* Слой приложения для работы с базой данных, использующий Spring Boot Data JPA.
* Спецификации для фильтрации отелей и комнат.
* Маппинг сущностей с использованием MapStruct.
* Документация API
* Spring Security
* Apache Kafka
* PostgreSQL
* MongoDB
* Testcontainers
* JaCoCo
* Liquibase

## Функциональность API
API предоставляет следующие функции:
1. Создание пользователей.
2. Создание отелей и управление ими.
3. Создание комнат и управление ими.
4. Бронирование комнат.
5. Статистическая информация доступна для выгрузки в CSV-файлы.

### Документация API
Документацию API можно найти по ссылке: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

