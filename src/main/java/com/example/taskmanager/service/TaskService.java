package com.example.taskmanager.service;

import com.example.taskmanager.dao.TaskDao;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.util.TaskValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой для работы с задачами. Содержит бизнес-логику: создание, обновление, удаление,
 * получение и поиск задач. Перед сохранением выполняет валидацию через {@link TaskValidator}.
 *
 * @param taskDao DAO для доступа к данным задач
 * @author Shebeta N.I.
 */
public record TaskService(TaskDao taskDao) {

  /**
   * Создаёт новую задачу с указанными параметрами. Устанавливает даты создания и обновления в
   * текущий момент времени, выполняет валидацию и сохраняет через DAO.
   *
   * @param title       заголовок задачи (не null/пустой)
   * @param description описание задачи (может быть пустым)
   * @param status      статус задачи (не null)
   * @return созданная задача с присвоенным идентификатором
   * @throws com.example.taskmanager.exception.ValidationException если валидация не пройдена
   * @throws com.example.taskmanager.exception.DaoException        при ошибке доступа к БД
   */
  public Task createTask(String title, String description, TaskStatus status) {
    Task task = new Task();
    task.setTitle(title);
    task.setDescription(description);
    task.setStatus(status);
    task.setCreatedAt(LocalDateTime.now());
    task.setUpdatedAt(LocalDateTime.now());
    TaskValidator.validate(task);
    return taskDao.save(task);
  }

  /**
   * Обновляет существующую задачу. Устанавливает дату обновления в текущий момент, выполняет
   * валидацию и сохраняет изменения через DAO.
   *
   * @param task задача с обновлёнными полями (должен быть установлен id)
   * @return обновлённая задача
   * @throws com.example.taskmanager.exception.ValidationException если валидация не пройдена
   * @throws com.example.taskmanager.exception.DaoException        при ошибке доступа к БД
   */
  public Task updateTask(Task task) {
    task.setUpdatedAt(LocalDateTime.now());
    TaskValidator.validate(task);
    return taskDao.save(task);
  }

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @throws com.example.taskmanager.exception.DaoException при ошибке доступа к БД
   */
  public void deleteTask(int id) {
    taskDao.delete(id);
  }

  /**
   * Возвращает задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей, если найдена, иначе пустой Optional
   * @throws com.example.taskmanager.exception.DaoException при ошибке доступа к БД
   */
  public Optional<Task> getTask(int id) {
    return taskDao.findById(id);
  }

  /**
   * Возвращает все задачи.
   *
   * @return список всех задач (может быть пустым)
   * @throws com.example.taskmanager.exception.DaoException при ошибке доступа к БД
   */
  public List<Task> getAllTasks() {
    return taskDao.findAll();
  }

  /**
   * Возвращает задачи с указанным статусом.
   *
   * @param status статус для фильтрации
   * @return список задач с заданным статусом
   * @throws com.example.taskmanager.exception.DaoException при ошибке доступа к БД
   */
  public List<Task> getTasksByStatus(TaskStatus status) {
    return taskDao.findByStatus(status);
  }

  /**
   * Выполняет поиск задач по подстроке в заголовке или описании.
   *
   * @param query строка поиска (может быть пустой)
   * @return список найденных задач
   * @throws com.example.taskmanager.exception.DaoException при ошибке доступа к БД
   */
  public List<Task> searchTasks(String query) {
    return taskDao.search(query);
  }
}