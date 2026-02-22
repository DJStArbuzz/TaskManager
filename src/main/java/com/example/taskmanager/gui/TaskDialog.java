package com.example.taskmanager.gui;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.ValidationException;
import com.example.taskmanager.service.TaskService;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно для создания или редактирования задачи. Содержит поля для ввода заголовка,
 * описания и выбора статуса.
 *
 * @author Shebeta N.I.
 */
public class TaskDialog extends JDialog {

  private final TaskService taskService;
  private final Task taskToEdit; // null для создания новой задачи
  private JTextField titleField;
  private JTextArea descriptionArea;
  private JComboBox<TaskStatus> statusCombo;

  /**
   * Конструктор для создания новой задачи.
   *
   * @param owner       родительское окно
   * @param title       заголовок диалога
   * @param taskService сервис для сохранения задачи
   */
  public TaskDialog(Frame owner, String title, TaskService taskService) {
    this(owner, title, taskService, null);
  }

  /**
   * Конструктор для редактирования существующей задачи.
   *
   * @param owner       родительское окно
   * @param title       заголовок диалога
   * @param taskService сервис для сохранения задачи
   * @param taskToEdit  редактируемая задача (не null)
   */
  public TaskDialog(Frame owner, String title, TaskService taskService, Task taskToEdit) {
    super(owner, title, true);
    this.taskService = taskService;
    this.taskToEdit = taskToEdit;
    initComponents();
    if (taskToEdit != null) {
      fillFields();
    }
    setLocationRelativeTo(owner);
  }

  /**
   * Инициализирует компоненты диалога: поля ввода, кнопки.
   */
  private void initComponents() {
    setLayout(new BorderLayout(10, 10));
    setSize(450, 350);
    setResizable(false);

    // Панель с полями ввода
    JPanel inputPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Заголовок
    gbc.gridx = 0;
    gbc.gridy = 0;
    inputPanel.add(new JLabel("Заголовок:*"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1.0;
    titleField = new JTextField(30);
    inputPanel.add(titleField, gbc);

    // Описание
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    inputPanel.add(new JLabel("Описание:"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1.0;
    descriptionArea = new JTextArea(5, 30);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(descriptionArea);
    inputPanel.add(scrollPane, gbc);

    // Статус
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.weightx = 0;
    inputPanel.add(new JLabel("Статус:*"), gbc);
    gbc.gridx = 1;
    statusCombo = new JComboBox<>(TaskStatus.values());
    inputPanel.add(statusCombo, gbc);

    add(inputPanel, BorderLayout.CENTER);

    // Панель кнопок
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton saveButton = new JButton("Сохранить");
    JButton cancelButton = new JButton("Отмена");

    saveButton.addActionListener(e -> saveTask());
    cancelButton.addActionListener(e -> dispose());

    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  /**
   * Заполняет поля данными из редактируемой задачи.
   */
  private void fillFields() {
    titleField.setText(taskToEdit.getTitle());
    descriptionArea.setText(taskToEdit.getDescription());
    statusCombo.setSelectedItem(taskToEdit.getStatus());
  }

  /**
   * Сохраняет задачу (новую или обновлённую). Валидирует данные через сервис и обрабатывает
   * возможные исключения.
   */
  private void saveTask() {
    String title = titleField.getText().trim();
    String description = descriptionArea.getText().trim();
    TaskStatus status = (TaskStatus) statusCombo.getSelectedItem();

    try {
      if (taskToEdit == null) {
        taskService.createTask(title, description, status);
      } else {
        taskToEdit.setTitle(title);
        taskToEdit.setDescription(description);
        taskToEdit.setStatus(status);
        taskService.updateTask(taskToEdit);
      }
      dispose(); // закрыть диалог при успехе
    } catch (ValidationException ex) {
      JOptionPane.showMessageDialog(this,
          "Ошибка валидации:\n" + ex.getMessage(),
          "Некорректные данные",
          JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "Ошибка при сохранении:\n" + ex.getMessage(),
          "Ошибка",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}