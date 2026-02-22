package com.example.taskmanager.gui;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.service.TaskService;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Главное окно приложения для управления задачами. Содержит таблицу задач, панель фильтрации/поиска
 * и кнопки для добавления, редактирования и удаления задач.
 *
 * @author Shebeta N.I.
 */
public class MainFrame extends JFrame {

  private final TaskService taskService;
  private JTable taskTable;
  private TaskTableModel tableModel;
  private JComboBox<String> statusFilterCombo;
  private JTextField searchField;

  /**
   * Создаёт главное окно и инициализирует компоненты.
   *
   * @param taskService сервис для работы с задачами (не может быть null)
   */
  public MainFrame(TaskService taskService) {
    this.taskService = taskService;
    initComponents();
    loadAllTasks();
    setLocationRelativeTo(null);
  }

  /**
   * Инициализирует графические компоненты: таблицу, панели фильтрации и кнопок.
   */
  private void initComponents() {
    setTitle("Управление задачами");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600);
    setLayout(new BorderLayout());

    // Создание таблицы
    tableModel = new TaskTableModel(List.of());
    taskTable = new JTable(tableModel);
    taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    taskTable.setRowHeight(28);
    taskTable.getTableHeader().setReorderingAllowed(false);

    // Сортировка по столбцам
    TableRowSorter<TaskTableModel> sorter = new TableRowSorter<>(tableModel);
    taskTable.setRowSorter(sorter);

    JScrollPane scrollPane = new JScrollPane(taskTable);
    add(scrollPane, BorderLayout.CENTER);

    // Верхняя панель с фильтром по статусу и поиском
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(new JLabel("Статус:"));

    statusFilterCombo = new JComboBox<>(new String[]{"Все", "TODO", "IN_PROGRESS", "DONE"});
    statusFilterCombo.addActionListener(e -> applyFilter());
    topPanel.add(statusFilterCombo);

    topPanel.add(new JLabel("Поиск:"));
    searchField = new JTextField(15);
    topPanel.add(searchField);

    JButton searchButton = new JButton("Найти");
    searchButton.addActionListener(e -> searchTasks());
    topPanel.add(searchButton);

    add(topPanel, BorderLayout.NORTH);

    add(createButtonPanel(), BorderLayout.SOUTH);
  }

  /**
   * Создаёт панель с кнопками "Добавить", "Редактировать", "Удалить".
   *
   * @return панель с кнопками
   */
  private JPanel createButtonPanel() {
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton addButton = new JButton("Добавить");
    JButton editButton = new JButton("Редактировать");
    JButton deleteButton = new JButton("Удалить");

    addButton.addActionListener(e -> openAddDialog());
    editButton.addActionListener(e -> openEditDialog());
    deleteButton.addActionListener(e -> deleteSelectedTask());

    bottomPanel.add(addButton);
    bottomPanel.add(editButton);
    bottomPanel.add(deleteButton);
    return bottomPanel;
  }

  /**
   * Загружает все задачи из сервиса и обновляет таблицу. Сбрасывает фильтр на "Все".
   */
  private void loadAllTasks() {
    List<Task> tasks = taskService.getAllTasks();
    tableModel.setTasks(tasks);
    statusFilterCombo.setSelectedIndex(0);
  }

  /**
   * Применяет выбранный фильтр по статусу. Если выбран пункт "Все", загружаются все задачи, иначе —
   * задачи с указанным статусом.
   */
  private void applyFilter() {
    String selected = (String) statusFilterCombo.getSelectedItem();
    if ("Все".equals(selected)) {
      loadAllTasks();
    } else {
      TaskStatus status = TaskStatus.valueOf(selected);
      List<Task> tasks = taskService.getTasksByStatus(status);
      tableModel.setTasks(tasks);
    }
  }

  /**
   * Выполняет поиск задач по подстроке в заголовке или описании. Если строка поиска пуста,
   * загружает все задачи.
   */
  private void searchTasks() {
    String query = searchField.getText().trim();
    if (query.isEmpty()) {
      loadAllTasks();
    } else {
      List<Task> tasks = taskService.searchTasks(query);
      tableModel.setTasks(tasks);
    }
  }

  /**
   * Открывает диалог для добавления новой задачи.
   */
  private void openAddDialog() {
    TaskDialog dialog = new TaskDialog(this, "Добавление задачи", taskService);
    dialog.setVisible(true);
    loadAllTasks(); // обновляем таблицу после закрытия диалога
  }

  /**
   * Открывает диалог для редактирования выбранной задачи. Если задача не выбрана, показывает
   * предупреждение.
   */
  private void openEditDialog() {
    int selectedRow = taskTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Выберите задачу для редактирования",
          "Ошибка",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    int modelRow = taskTable.convertRowIndexToModel(selectedRow);
    Task task = tableModel.getTaskAt(modelRow);
    TaskDialog dialog = new TaskDialog(this, "Редактирование задачи", taskService, task);
    dialog.setVisible(true);
    loadAllTasks();
  }

  /**
   * Удаляет выбранную задачу после подтверждения пользователя. Если задача не выбрана, показывает
   * предупреждение.
   */
  private void deleteSelectedTask() {
    int selectedRow = taskTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Выберите задачу для удаления",
          "Ошибка",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    int modelRow = taskTable.convertRowIndexToModel(selectedRow);
    Task task = tableModel.getTaskAt(modelRow);

    int confirm = JOptionPane.showConfirmDialog(this,
        "Удалить задачу \"" + task.getTitle() + "\"?",
        "Подтверждение удаления",
        JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      taskService.deleteTask(task.getId());
      loadAllTasks();
    }
  }
}