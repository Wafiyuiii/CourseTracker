import java.util.Scanner;
import java.util.List;

public class Main {
    private static DatabaseHandler db = new DatabaseHandler();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Course Tracker ===");

        while (true) {
            System.out.println("\n1. Add New Course");
            System.out.println("2. Add Assignment");
            System.out.println("3. Add Quiz");
            System.out.println("4. Update Grade");
            System.out.println("5. Show Grades");
            System.out.println("6. Exit");

            System.out.print("Choose (1-6): ");

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> addCourse();
                    case 2 -> addAssignment();
                    case 3 -> addQuiz();
                    case 4 -> updateGrade();
                    case 5 -> showGrades();
                    case 6 -> {
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number 1-6.");
            }
        }
    }

    private static void addCourse() {
        System.out.print("Course Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Instructor: ");
        String instructor = scanner.nextLine().trim();

        db.addCourse(name, instructor);
        System.out.println("Course added.");
    }

    private static void addAssignment() {
        List<String> courses = db.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }

        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));
        }
        System.out.print("Choose number: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();
        if (index < 0 || index >= courses.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String courseName = courses.get(index).split(" - ")[0];

        System.out.print("Assignment Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine().trim();

        if (db.addGradedItem(courseName, "Assignment", name, dueDate)) {
            System.out.println("Assignment added.");
        }
    }

    private static void addQuiz() {
        List<String> courses = db.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }

        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));
        }
        System.out.print("Choose number: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();
        if (index < 0 || index >= courses.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String courseName = courses.get(index).split(" - ")[0];

        System.out.print("Quiz Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine().trim();

        if (db.addGradedItem(courseName, "Quiz", name, dueDate)) {
            System.out.println("Quiz added.");
        }
    }

    private static void updateGrade() {
        List<String> courses = db.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }

        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));
        }
        System.out.print("Choose number: ");
        int courseIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (courseIndex < 0 || courseIndex >= courses.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String courseName = courses.get(courseIndex).split(" - ")[0];

        System.out.print("Type (Assignment or Quiz): ");
        String type = scanner.nextLine().trim();

        List<String> items = db.getGradedItemsByCourseAndType(courseName, type);
        if (items.isEmpty()) {
            System.out.println("No " + type + "s found.");
            return;
        }

        System.out.println("Select item:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i+1) + ". " + items.get(i));
        }
        System.out.print("Choose number: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String itemName = items.get(itemIndex);

        System.out.print("Enter grade (0-100): ");
        double grade = scanner.nextDouble();
        scanner.nextLine();

        if (db.updateGrade(courseName, type, itemName, grade)) {
            System.out.println("Grade updated.");
        }
    }

    private static void showGrades() {
        List<String> courses = db.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }

        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));
        }
        System.out.print("Choose number: ");
        int courseIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (courseIndex < 0 || courseIndex >= courses.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String courseName = courses.get(courseIndex).split(" - ")[0];

        System.out.println("\n1. Overview");
        System.out.println("2. Assignments");
        System.out.println("3. Quizzes");
        System.out.print("Choose (1-3): ");
        int option = scanner.nextInt();
        scanner.nextLine();

        String report = db.getGradesReport(courseName, option == 1 ? "Overview" : option == 2 ? "Assignments" : "Quizzes");
        System.out.println("\n" + report);
    }
}
