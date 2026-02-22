package com.example.taskmanager.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Запускает миграции базы данных с помощью Liquibase.
 * <p>
 * Класс содержит статический метод {@link #runMigrations()}, который инициирует обновление схемы БД
 * до актуальной версии, используя changelog-файлы, расположенные в {@code db/changelog/}.
 * </p>
 *
 * @author Shebeta N.I.
 */
public final class LiquibaseRunner {

  private LiquibaseRunner() {
    // Предотвращение создания экземпляров утилитного класса
  }

  /**
   * Выполняет миграции Liquibase. Получает соединение через
   * {@link DatabaseConnection#getConnection()}, создает объект {@link Liquibase} с указанием
   * master-файла changelog и вызывает {@code update()}.
   *
   * @throws RuntimeException если возникает ошибка при работе с базой данных или Liquibase
   */
  public static void runMigrations() {
    try (Connection connection = DatabaseConnection.getConnection()) {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(
          "db/changelog/db.changelog-master.xml",
          new ClassLoaderResourceAccessor(),
          database);
      liquibase.update();
    } catch (SQLException | LiquibaseException e) {
      throw new RuntimeException("Ошибка применения миграций Liquibase", e);
    }
  }
}