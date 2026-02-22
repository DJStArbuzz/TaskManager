package com.example.taskmanager.gui;

import com.example.taskmanager.entity.Task;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Модель таблицы для отображения списка задач в {@link JTable}. Содержит шесть столбцов: ID,
 * заголовок, описание, статус, дата создания, дата обновления. Даты форматируются по шаблону
 * "yyyy-MM-dd HH:mm".
 *
 * @author Shebeta N.I.
 */
public class TaskTableModel extends AbstractTableModel {

  private final String[] columns = {"ID", "Заголовок", "Описание", "Статус", "Создано",
      "Обновлено"};
  private List<Task> tasks;

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  /**
   * Создаёт модель таблицы с начальным списком задач.
   *
   * @param tasks список задач для отображения
   */
  public TaskTableModel(List<Task> tasks) {
    this.tasks = tasks;
  }

  /**
   * Обновляет данные модели и уведомляет таблицу об изменениях.
   *
   * @param tasks новый список задач
   */
  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
    fireTableDataChanged();
  }

  /**
   * Возвращает задачу по индексу строки (в модели, с учётом сортировки/фильтрации).
   *
   * @param rowIndex индекс строки в модели
   * @return задача, соответствующая указанной строке
   */
  public Task getTaskAt(int rowIndex) {
    return tasks.get(rowIndex);
  }

  /**
   * Возвращает количество строк (задач) в таблице.
   *
   * @return количество задач
   */
  @Override
  public int getRowCount() {
    return tasks.size();
  }

  /**
   * Возвращает количество столбцов в таблице.
   *
   * @return количество столбцов
   */
  @Override
  public int getColumnCount() {
    return columns.length;
  }

  /**
   * Возвращает имя столбца по его индексу.
   *
   * @param column индекс столбца (0..5)
   * @return локализованное название столбца
   */
  @Override
  public String getColumnName(int column) {
    return columns[column];
  }

  /**
   * Возвращает значение ячейки по координатам.
   *
   * @param rowIndex    индекс строки
   * @param columnIndex индекс столбца
   * @return значение ячейки (тип зависит от столбца: Integer, String, TaskStatus, String)
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Task task = tasks.get(rowIndex);
    return switch (columnIndex) {
      case 0 -> task.getId();
      case 1 -> task.getTitle();
      case 2 -> task.getDescription();
      case 3 -> task.getStatus();
      case 4 -> task.getCreatedAt() != null
          ? task.getCreatedAt().format(DATE_TIME_FORMATTER)
          : "";
      case 5 -> task.getUpdatedAt() != null
          ? task.getUpdatedAt().format(DATE_TIME_FORMATTER)
          : "";
      default -> null;
    };
  }
}