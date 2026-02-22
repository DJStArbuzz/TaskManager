package com.example.taskmanager.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляет задачу в системе управления задачами. Содержит поля: идентификатор, заголовок,
 * описание, статус, дату создания и дату обновления.
 *
 * @author Shebeta N.I.
 */
public class Task {

  private int id;
  private String title;
  private String description;
  private TaskStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  /**
   * Конструктор по умолчанию.
   */
  public Task() {
  }

  /**
   * Конструктор для создания полностью инициализированной задачи.
   *
   * @param id          идентификатор задачи
   * @param title       заголовок
   * @param description описание
   * @param status      статус
   * @param createdAt   дата и время создания
   * @param updatedAt   дата и время последнего обновления
   */
  public Task(int id, String title, String description, TaskStatus status,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  /**
   * Возвращает идентификатор задачи.
   *
   * @return идентификатор
   */
  public int getId() {
    return id;
  }

  /**
   * Устанавливает идентификатор задачи.
   *
   * @param id идентификатор
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Возвращает заголовок задачи.
   *
   * @return заголовок
   */
  public String getTitle() {
    return title;
  }

  /**
   * Устанавливает заголовок задачи.
   *
   * @param title заголовок
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Возвращает описание задачи.
   *
   * @return описание
   */
  public String getDescription() {
    return description;
  }

  /**
   * Устанавливает описание задачи.
   *
   * @param description описание
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Возвращает статус задачи.
   *
   * @return статус
   */
  public TaskStatus getStatus() {
    return status;
  }

  /**
   * Устанавливает статус задачи.
   *
   * @param status статус
   */
  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  /**
   * Возвращает дату и время создания задачи.
   *
   * @return дата создания
   */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /**
   * Устанавливает дату и время создания задачи.
   *
   * @param createdAt дата создания
   */
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * Возвращает дату и время последнего обновления задачи.
   *
   * @return дата обновления
   */
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Устанавливает дату и время последнего обновления задачи.
   *
   * @param updatedAt дата обновления
   */
  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * Сравнивает задачу с другим объектом. Две задачи считаются равными, если они имеют одинаковый
   * идентификатор.
   *
   * @param o объект для сравнения
   * @return {@code true}, если объекты равны; иначе {@code false}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    return id == task.id;
  }

  /**
   * Вычисляет хеш-код задачи на основе её идентификатора.
   *
   * @return хеш-код
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Возвращает строковое представление задачи, содержащее идентификатор, заголовок и статус.
   *
   * @return строковое представление
   */
  @Override
  public String toString() {
    return "Task{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", status=" + status +
        '}';
  }
}