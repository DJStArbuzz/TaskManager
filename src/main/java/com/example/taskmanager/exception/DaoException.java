package com.example.taskmanager.exception;

/**
 * Исключение уровня доступа к данным (DAO). Выбрасывается при ошибках взаимодействия с базой
 * данных, например, при SQL-ошибках или проблемах соединения.
 *
 * @author Shebeta N.I.
 */
public class DaoException extends RuntimeException {

  /**
   * Создаёт исключение с сообщением и причиной.
   *
   * @param message детальное сообщение об ошибке
   * @param cause   исходная причина исключения (обычно SQLException)
   */
  public DaoException(String message, Throwable cause) {
    super(message, cause);
  }
}