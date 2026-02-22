package com.example.taskmanager;

import com.example.taskmanager.dao.TaskDao;
import com.example.taskmanager.dao.TaskDaoImpl;
import com.example.taskmanager.db.DatabaseConnection;
import com.example.taskmanager.db.LiquibaseRunner;
import com.example.taskmanager.gui.MainFrame;
import com.example.taskmanager.service.TaskService;

import javax.swing.*;

/**
 * Главный класс приложения для управления задачами. Выполняет инициализацию базы данных (миграции
 * Liquibase), создаёт необходимые компоненты (DAO, сервис) и запускает графический интерфейс. Также
 * регистрирует shutdown hook для корректного закрытия пула соединений.
 *
 * @author Shebeta N.I.
 */
public class Application {

  /**
   * Точка входа в приложение.
   *
   * @param args аргументы командной строки (не используются)
   */
  public static void main(String[] args) {
    // LiquibaseRunner.runMigrations();

    TaskDao taskDao = new TaskDaoImpl();
    TaskService taskService = new TaskService(taskDao);

    // Запуск GUI в потоке обработки событий
    SwingUtilities.invokeLater(() -> {
      MainFrame mainFrame = new MainFrame(taskService);
      mainFrame.setVisible(true);
    });

    // Закрытие пула соединений при завершении приложения
    Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::close));
  }
}