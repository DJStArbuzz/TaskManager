package com.example.taskmanager.exception;

/**
 * Исключение, выбрасываемое при нарушении валидации входных данных. Например, если заголовок задачи
 * пуст или превышает допустимую длину.
 *
 * @author Shebeta N.I.
 */
public class ValidationException extends RuntimeException {

  /**
   * Создаёт исключение с указанным сообщением.
   *
   * @param message детали ошибки валидации
   */
  public ValidationException(String message) {
    super(message);
  }
}