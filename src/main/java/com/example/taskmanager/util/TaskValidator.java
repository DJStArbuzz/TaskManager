package com.example.taskmanager.util;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.ValidationException;

/**
 * Утилитный класс для валидации полей задачи. Содержит статические методы проверки корректности
 * данных перед сохранением.
 *
 * @author Shebeta N.I.
 */
public final class TaskValidator {

  private TaskValidator() {
    // Предотвращение создания экземпляров утилитного класса
  }

  /**
   * Проверяет задачу на соответствие бизнес-правилам:
   * <ul>
   *   <li>Заголовок не может быть null или пустым</li>
   *   <li>Заголовок не длиннее 255 символов</li>
   *   <li>Статус не может быть null</li>
   *   <li>Описание, если задано, не длиннее 1000 символов</li>
   * </ul>
   *
   * @param task объект задачи для проверки
   * @throws ValidationException если какое-либо правило нарушено
   */
  public static void validate(Task task) {
    if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
      throw new ValidationException("Заголовок не может быть пустым");
    }
    if (task.getTitle().length() > 255) {
      throw new ValidationException("Заголовок не должен превышать 255 символов");
    }
    if (task.getStatus() == null) {
      throw new ValidationException("Статус должен быть указан");
    }
    if (task.getDescription() != null && task.getDescription().length() > 1000) {
      throw new ValidationException("Описание слишком длинное (максимум 1000 символов)");
    }
  }
}