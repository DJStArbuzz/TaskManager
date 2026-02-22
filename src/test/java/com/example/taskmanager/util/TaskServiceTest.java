package com.example.taskmanager.util;

import com.example.taskmanager.dao.TaskDao;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.ValidationException;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Модульные тесты для сервиса {@link TaskService}. Используют Mockito для изоляции от реального
 * DAO.
 *
 * @author Shebeta N.I.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskDao taskDao;

  @Captor
  private ArgumentCaptor<Task> taskCaptor;

  private TaskService taskService;

  @BeforeEach
  void setUp() {
    taskService = new TaskService(taskDao);
  }

  /**
   * Тестирует создание задачи: проверяет установку дат и вызов DAO.
   */
  @Test
  void createTask_shouldSetDatesAndCallDaoSave() {
    // given
    String title = "Test Task";
    String description = "Test Description";
    TaskStatus status = TaskStatus.TODO;

    // when
    Task result = taskService.createTask(title, description, status);

    // then
    verify(taskDao).save(taskCaptor.capture());
    Task savedTask = taskCaptor.getValue();

    assertEquals(title, savedTask.getTitle());
    assertEquals(description, savedTask.getDescription());
    assertEquals(status, savedTask.getStatus());
    assertNotNull(savedTask.getCreatedAt());
    assertNotNull(savedTask.getUpdatedAt());
    assertTrue(savedTask.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
  }

  /**
   * Тестирует обновление задачи: проверяет обновление поля updatedAt и вызов DAO.
   */
  @Test
  void updateTask_shouldUpdateUpdatedAtAndCallDaoSave() {
    // given
    Task existingTask = new Task();
    existingTask.setId(1);
    existingTask.setTitle("Old Title");
    existingTask.setDescription("Old Desc");
    existingTask.setStatus(TaskStatus.TODO);
    existingTask.setCreatedAt(LocalDateTime.now().minusDays(1));
    existingTask.setUpdatedAt(LocalDateTime.now().minusDays(1));

    when(taskDao.save(any(Task.class))).thenReturn(existingTask);

    // when
    existingTask.setTitle("New Title");
    Task result = taskService.updateTask(existingTask);

    // then
    verify(taskDao).save(taskCaptor.capture());
    Task updatedTask = taskCaptor.getValue();

    assertEquals("New Title", updatedTask.getTitle());
    assertTrue(updatedTask.getUpdatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    // created_at не должен меняться
    assertEquals(existingTask.getCreatedAt(), updatedTask.getCreatedAt());
  }

  /**
   * Тестирует, что при попытке создать задачу с пустым заголовком выбрасывается исключение
   * {@link ValidationException}.
   */
  @Test
  void createTask_shouldThrowExceptionWhenTitleIsEmpty() {
    assertThrows(ValidationException.class,
        () -> taskService.createTask("", "desc", TaskStatus.TODO));
  }
}