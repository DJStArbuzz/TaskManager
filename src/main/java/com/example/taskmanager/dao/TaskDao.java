package com.example.taskmanager.dao;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.DaoException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс Data Access Object (DAO) для работы с задачами. Предоставляет методы для выполнения
 * операций CRUD и поиска задач в базе данных.
 *
 * @author Shebeta N.I.
 */
public interface TaskDao {

  /**
   * Сохраняет задачу в базе данных. Если задача новая (id == 0), выполняется вставка, иначе —
   * обновление существующей записи.
   *
   * @param task объект задачи, который необходимо сохранить
   * @return сохранённая задача с присвоенным идентификатором (для новой задачи)
   * @throws DaoException если происходит ошибка доступа к БД
   */
  Task save(Task task);

  /**
   * Находит задачу по её идентификатору.
   *
   * @param id уникальный идентификатор задачи
   * @return {@code Optional}, содержащий найденную задачу, или пустой {@code Optional}, если задача
   * не найдена
   * @throws DaoException если происходит ошибка доступа к БД
   */
  Optional<Task> findById(int id);

  /**
   * Возвращает список всех задач, отсортированных по идентификатору.
   *
   * @return список всех задач (может быть пустым)
   * @throws DaoException если происходит ошибка доступа к БД
   */
  List<Task> findAll();

  /**
   * Возвращает список задач с указанным статусом, отсортированных по идентификатору.
   *
   * @param status задачи (например, NEW, IN_PROGRESS, DONE)
   * @return список задач с заданным статусом
   * @throws DaoException если происходит ошибка доступа к БД
   */
  List<Task> findByStatus(TaskStatus status);

  /**
   * Выполняет поиск задач по частичному совпадению в заголовке или описании (регистронезависимый
   * поиск).
   *
   * @param query строка поиска (может быть пустой или частичной)
   * @return список задач, удовлетворяющих условию поиска
   * @throws DaoException если происходит ошибка доступа к БД
   */
  List<Task> search(String query);

  /**
   * Удаляет задачу по её идентификатору.
   *
   * @param id идентификатор задачи, которую необходимо удалить
   * @throws DaoException если происходит ошибка доступа к БД
   */
  void delete(int id);
}