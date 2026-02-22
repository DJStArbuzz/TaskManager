package com.example.taskmanager.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Утилитный класс для управления подключением к базе данных через HikariCP.
 * <p>
 * При загрузке класса инициализируется пул соединений на основе параметров из файла
 * {@code application.properties}, расположенного в classpath. Предоставляет методы для получения
 * соединения и закрытия пула.
 * </p>
 *
 * @author Shebeta N.I.
 */
public final class DatabaseConnection {

  private static final HikariDataSource dataSource;

  static {
    // Загрузка конфигурации из application.properties и настройка пула соединений
    try (InputStream input = DatabaseConnection.class.getClassLoader()
        .getResourceAsStream("application.properties")) {
      if (input == null) {
        throw new RuntimeException("Файл application.properties не найден в classpath");
      }

      Properties props = new Properties();
      props.load(input);

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(props.getProperty("db.url"));
      config.setUsername(props.getProperty("db.username"));
      config.setPassword(props.getProperty("db.password"));
      config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.poolSize", "10")));
      config.setConnectionTimeout(30000);
      config.setIdleTimeout(600000);
      config.setMaxLifetime(1800000);

      dataSource = new HikariDataSource(config);
    } catch (IOException e) {
      throw new RuntimeException("Не удалось загрузить конфигурацию БД", e);
    }
  }

  private DatabaseConnection() {
    // Предотвращение создания экземпляров утилитного класса
  }

  /**
   * Получает соединение из пула.
   *
   * @return соединение с базой данных
   * @throws SQLException если не удалось получить соединение
   */
  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  /**
   * Закрывает пул соединений. После вызова этого метода все последующие вызовы
   * {@link #getConnection()} будут выбрасывать исключение.
   */
  public static void close() {
    if (dataSource != null) {
      dataSource.close();
    }
  }
}