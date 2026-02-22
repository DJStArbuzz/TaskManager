package com.example.taskmanager.dao;

import com.example.taskmanager.db.DatabaseConnection;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.DaoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса {@link TaskDao} для работы с PostgreSQL через JDBC. Выполняет операции
 * вставки, обновления, удаления и выборки задач из таблицы "tasks".
 *
 * @author Shebeta N.I.
 */
public class TaskDaoImpl implements TaskDao {

  /**
   * Сохраняет задачу. Если у задачи не задан id (равен 0), выполняется вставка новой записи, иначе
   * — обновление существующей.
   *
   * @param task объект задачи для сохранения
   * @return сохранённая задача с установленным id (для новой записи)
   * @throws DaoException при ошибке SQL
   */
  @Override
  public Task save(Task task) {
    if (task.getId() == 0) {
      return insert(task);
    } else {
      return update(task);
    }
  }

  /**
   * Вставляет новую задачу в таблицу. Генерирует новый идентификатор через автоинкремент.
   *
   * @param task задача для вставки (id должен быть 0)
   * @return та же задача с заполненным id и временными метками
   * @throws DaoException если не удалось выполнить вставку
   */
  private Task insert(Task task) {
    String sql = "INSERT INTO tasks (title, description, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, task.getTitle());
      stmt.setString(2, task.getDescription());
      stmt.setString(3, task.getStatus().name());
      stmt.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));
      stmt.setTimestamp(5, Timestamp.valueOf(task.getUpdatedAt()));
      stmt.executeUpdate();

      ResultSet generatedKeys = stmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        task.setId(generatedKeys.getInt(1));
      }
      return task;
    } catch (SQLException e) {
      throw new DaoException("Ошибка вставки задачи", e);
    }
  }

  /**
   * Обновляет существующую задачу в таблице.
   *
   * @param task задача с уже существующим id
   * @return обновлённая задача
   * @throws DaoException если не удалось выполнить обновление
   */
  private Task update(Task task) {
    String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, updated_at = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, task.getTitle());
      stmt.setString(2, task.getDescription());
      stmt.setString(3, task.getStatus().name());
      stmt.setTimestamp(4, Timestamp.valueOf(task.getUpdatedAt()));
      stmt.setInt(5, task.getId());
      stmt.executeUpdate();
      return task;
    } catch (SQLException e) {
      throw new DaoException("Ошибка обновления задачи с id=" + task.getId(), e);
    }
  }

  /**
   * Ищет задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей, если найдена, иначе пустой Optional
   * @throws DaoException при ошибке SQL
   */
  @Override
  public Optional<Task> findById(int id) {
    String sql = "SELECT id, title, description, status, created_at, updated_at FROM tasks WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return Optional.of(mapRowToTask(rs));
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new DaoException("Ошибка поиска задачи по id=" + id, e);
    }
  }

  /**
   * Возвращает все задачи из таблицы, отсортированные по id.
   *
   * @return список всех задач (может быть пустым)
   * @throws DaoException при ошибке SQL
   */
  @Override
  public List<Task> findAll() {
    String sql = "SELECT id, title, description, status, created_at, updated_at FROM tasks ORDER BY id";
    List<Task> tasks = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        tasks.add(mapRowToTask(rs));
      }
      return tasks;
    } catch (SQLException e) {
      throw new DaoException("Ошибка получения всех задач", e);
    }
  }

  /**
   * Возвращает задачи с заданным статусом.
   *
   * @param status статус для фильтрации
   * @return список задач с указанным статусом
   * @throws DaoException при ошибке SQL
   */
  @Override
  public List<Task> findByStatus(TaskStatus status) {
    String sql = "SELECT id, title, description, status, created_at, updated_at FROM tasks WHERE status = ? ORDER BY id";
    List<Task> tasks = new ArrayList<>();
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, status.name());
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        tasks.add(mapRowToTask(rs));
      }
      return tasks;
    } catch (SQLException e) {
      throw new DaoException("Ошибка поиска задач по статусу " + status, e);
    }
  }

  /**
   * Выполняет поиск задач, у которых заголовок или описание содержат заданную подстроку
   * (регистронезависимо).
   *
   * @param query строка для поиска
   * @return список найденных задач
   * @throws DaoException при ошибке SQL
   */
  @Override
  public List<Task> search(String query) {
    String sql = "SELECT id, title, description, status, created_at, updated_at FROM tasks " +
        "WHERE title ILIKE ? OR description ILIKE ? ORDER BY id";
    List<Task> tasks = new ArrayList<>();
    String searchPattern = "%" + query + "%";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, searchPattern);
      stmt.setString(2, searchPattern);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        tasks.add(mapRowToTask(rs));
      }
      return tasks;
    } catch (SQLException e) {
      throw new DaoException("Ошибка поиска задач по запросу: " + query, e);
    }
  }

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор удаляемой задачи
   * @throws DaoException при ошибке SQL
   */
  @Override
  public void delete(int id) {
    String sql = "DELETE FROM tasks WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DaoException("Ошибка удаления задачи с id=" + id, e);
    }
  }

  /**
   * Преобразует текущую строку {@link ResultSet} в объект {@link Task}.
   *
   * @param rs результат запроса, уже перемещённый на нужную строку
   * @return объект задачи, заполненный данными из текущей строки
   * @throws SQLException если возникает ошибка доступа к столбцам ResultSet
   */
  private Task mapRowToTask(ResultSet rs) throws SQLException {
    Task task = new Task();
    task.setId(rs.getInt("id"));
    task.setTitle(rs.getString("title"));
    task.setDescription(rs.getString("description"));
    task.setStatus(TaskStatus.valueOf(rs.getString("status")));
    task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    task.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
    return task;
  }
}