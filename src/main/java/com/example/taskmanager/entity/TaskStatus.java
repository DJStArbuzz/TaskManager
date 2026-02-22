package com.example.taskmanager.entity;

/**
 * Перечисление возможных статусов задачи.
 *
 * @author Shebeta N.I.
 */
public enum TaskStatus {
  /**
   * Задача только создана, работа ещё не начата.
   */
  TODO,

  /**
   * Задача находится в процессе выполнения.
   */
  IN_PROGRESS,

  /**
   * Задача выполнена.
   */
  DONE
}