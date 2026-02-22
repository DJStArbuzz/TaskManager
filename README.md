# Task Manager (Swing + PostgreSQL)

Учебное приложение для управления списком задач с графическим интерфейсом на Java Swing и хранением данных в PostgreSQL.  
Проект выполнен в рамках тестового задания на должность Java-разработчика в компанию ООО «Стандарт безопасности».

---

## Технологии

- Java 17
- PostgreSQL 16+
- Swing (GUI)
- JDBC + HikariCP (пул соединений)
- Liquibase (миграции БД)
- Maven (сборка)
- JUnit 5 + Mockito

---

## Требования к окружению

- Установленная Java 17 или выше
- Установленная PostgreSQL 16+ с созданной базой данных
- Maven 3.8+ (для сборки)
- Git (для клонирования)

---

## Настройка базы данных

1. Убедитесь, что PostgreSQL запущен (стандартный порт `5432`).
2. Создайте базу данных с именем **`taskdb`**.  
   Пример через командную строку `psql`:
   ```sql
   CREATE DATABASE taskdb;
Или через pgAdmin: нажмите правой кнопкой на Databases → Create → Database → введите имя taskdb.
3. Убедитесь, что пользователь postgres имеет пароль nexo2005 (именно этот пароль указан в файле конфигурации).
Если ваш пароль отличается, измените его в файле src/main/resources/application.properties.


Сборка и запуск
Вариант 1: Запуск из IntelliJ IDEA
Склонируйте репозиторий: git clone https://github.com/DJStArbuzz/TaskManager.git
Откройте проект в IntelliJ IDEA как проект Maven.
Дождитесь загрузки зависимостей. Запустите класс com.example.taskmanager.Application.

Вариант 2: Сборка через Maven и запуск JAR. 
```bash mvn clean package
java -jar target/untitled-1.0-SNAPSHOT.jar```

---
По всем вопросам обращайтесь: shebetanick123@yandex.ru
