import java.util.Scanner;
import java.util.List;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DatabaseHandler dbHandler = new DatabaseHandler();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Выберите действие: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    showAllTasks();
                    break;
                case 3:
                    showTaskById();
                    break;
                case 4:
                    updateTask();
                    break;
                case 5:
                    deleteTask();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        dbHandler.closeConnection();
        System.out.println("Программа завершена.");
    }

    private static void printMenu() {
        System.out.println("\n=== Список дел ===");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Показать все задачи");
        System.out.println("3. Показать задачу по ID");
        System.out.println("4. Обновить задачу");
        System.out.println("5. Удалить задачу");
        System.out.println("6. Выход");
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        return input;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void addTask() {
        System.out.println("\n--- Добавление новой задачи ---");
        String title = getStringInput("Введите название задачи: ");
        while (title.isEmpty()) {
            System.out.println("Название не может быть пустым!");
            title = getStringInput("Введите название задачи: ");
        }

        String description = getStringInput("Введите описание задачи (необязательно): ");
        String status = "NEW";

        Task task = new Task(title, description, status);
        dbHandler.addTask(task);
    }

    private static void showAllTasks() {
        System.out.println("\n--- Список всех задач ---");
        List<Task> tasks = dbHandler.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("Нет задач в списке.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
                System.out.println("---------------------");
            }
        }
    }

    private static void showTaskById() {
        System.out.println("\n--- Просмотр задачи по ID ---");
        int id = getIntInput("Введите ID задачи: ");
        Task task = dbHandler.getTaskById(id);

        if (task != null) {
            System.out.println("\nДетали задачи:");
            System.out.println("ID: " + task.getId());
            System.out.println("Название: " + task.getTitle());
            System.out.println("Описание: " + (task.getDescription() != null ? task.getDescription() : "Нет описания"));
            System.out.println("Статус: " + task.getStatus());
            System.out.println("Создана: " + task.getCreatedAt());
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    private static void updateTask() {
        System.out.println("\n--- Обновление задачи ---");
        int id = getIntInput("Введите ID задачи для обновления: ");
        Task task = dbHandler.getTaskById(id);

        if (task == null) {
            System.out.println("Задача с ID " + id + " не найдена.");
            return;
        }

        System.out.println("Текущие данные задачи:");
        System.out.println("1. Название: " + task.getTitle());
        System.out.println("2. Описание: " + (task.getDescription() != null ? task.getDescription() : "Нет описания"));
        System.out.println("3. Статус: " + task.getStatus());

        System.out.println("\nВведите новые данные (оставьте пустым, чтобы не изменять):");

        String newTitle = getStringInput("Новое название: ");
        if (!newTitle.isEmpty()) {
            task.setTitle(newTitle);
        }

        String newDescription = getStringInput("Новое описание: ");
        task.setDescription(newDescription.isEmpty() ? task.getDescription() : newDescription);

        System.out.println("Доступные статусы: NEW, IN_PROGRESS, DONE");
        String newStatus = getStringInput("Новый статус: ");
        if (!newStatus.isEmpty() && (newStatus.equals("NEW") || newStatus.equals("IN_PROGRESS") || newStatus.equals("DONE"))) {
            task.setStatus(newStatus);
        }

        dbHandler.updateTask(task);
    }

    private static void deleteTask() {
        System.out.println("\n--- Удаление задачи ---");
        int id = getIntInput("Введите ID задачи для удаления: ");

        String confirm = getStringInput("Вы уверены, что хотите удалить задачу с ID " + id + "? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            dbHandler.deleteTask(id);
        } else {
            System.out.println("Удаление отменено.");
        }
    }
}