package com.example.taskmanager.util;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для валидатора {@link TaskValidator}. Проверяют корректность обработки граничных
 * и некорректных значений.
 *
 * @author Shebeta N.I.
 */
class TaskValidatorTest {

  @Test
  void validate_shouldThrowExceptionWhenTitleIsNull() {
    Task task = new Task();
    task.setTitle(null);
    task.setStatus(TaskStatus.TODO);
    assertThrows(ValidationException.class, () -> TaskValidator.validate(task));
  }

  @Test
  void validate_shouldThrowExceptionWhenTitleIsEmpty() {
    Task task = new Task();
    task.setTitle("");
    task.setStatus(TaskStatus.TODO);
    assertThrows(ValidationException.class, () -> TaskValidator.validate(task));
  }

  @Test
  void validate_shouldThrowExceptionWhenStatusIsNull() {
    Task task = new Task();
    task.setTitle("Valid title");
    task.setStatus(null);
    assertThrows(ValidationException.class, () -> TaskValidator.validate(task));
  }

  @Test
  void validate_shouldPassWhenAllFieldsValid() {
    Task task = new Task();
    task.setTitle("Valid title");
    task.setStatus(TaskStatus.TODO);
    task.setDescription("Some description");
    assertDoesNotThrow(() -> TaskValidator.validate(task));
  }

}