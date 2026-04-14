import java.util.Scanner;
import java.util.List;

public class Main {

    //create database object 

    private static DatabaseHandler db = new DatabaseHandler();
    private static Scanner scanner = new Scanner(System.in);

    //show the text in terminal for user to chose and look into 
    public static void main(String[] args) {
        System.out.println("========= Course Tracker =========");

        while (true) {
            System.out.println("\n1. Add New Course");
            System.out.println("2. Add Assignment");
            System.out.println("3. Add Quiz");
            System.out.println("4. Update Grade");
            System.out.println("5. Show Grades");
            System.out.println("6. Exit");
            System.out.print("Choose (1-6): ");

            int input = scanner.nextInt();
            scanner.nextLine();   
            //user inputs will corresponds to the appropirate numbered method 
            if (input == 1) {
                addCourse();
            } else if (input == 2) {
                addAssignment();
            } else if (input == 3) {
                addQuiz();
            } else if (input == 4) {
                updateGrade();
            } else if (input == 5) {
                showGrades();
            } else if (input == 6) {
                System.out.println("See you next time!");
                scanner.close();
                return;
            } else {
                System.out.println("Invalid option, pick 1-6.");
            }
        }
    }


    //add course: It will let us add course name and instructor and save it in the db 

private static void addCourse() {
    System.out.print("Course Name: ");
    String name = scanner.nextLine().trim();
    System.out.print("Instructor: ");
    String instructor = scanner.nextLine().trim();

    Course c = new Course(name, instructor); //create course object
    db.addCourse(c);                        //pass the object 
    System.out.println("Course added.");
}

    //Funtion to remove repeating and DRY 
    
    private static String selectCourse() {

        //make a list and put the courses in them 

        List<String> courses = db.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return null; //show err if nothing in the course list
        }


        //loop over the coruse list and list them out as 1,2,3,4 
        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) { 
            System.out.println((i+1) + ". " + courses.get(i));
        }


        //let the user chose which course they want 
        System.out.print("Choose number: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index < 0 || index >= courses.size()) {//cant be less than 0 or more than the size itself
            System.out.println("Invalid choice.");
            return null;
        }

        return courses.get(index).split(" - ")[0]; //return only the coruse name not instructor
    }

    //add assingment , we call the same helper mehtod to list coruses 

    private static void addAssignment() {
        String courseName = selectCourse();
        if (courseName == null) return;

        //ask name and due date and store them

        System.out.print("Assignment Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine().trim();

        //save those varibles into the database

        Assignment a = new Assignment(name, dueDate);
        db.addGradedItem(courseName, a);   
        System.out.println("Assignment added.");
    }
//for quizzes its the same logic as assignments 
    private static void addQuiz() {
        String courseName = selectCourse();
        if (courseName == null) return;

        System.out.print("Quiz Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine().trim();

        Quiz q = new Quiz(name, dueDate);
        db.addGradedItem(courseName, q);
    }


    //this is where we update individual items's grades 

    private static void updateGrade() {
        String courseName = selectCourse(); //call the helper method to show courses 
        if (courseName == null) return;

        System.out.print("Type (Assignment or Quiz): "); //ask user to type Assignment or quiz
        String type = scanner.nextLine().trim();

        List<String> items = db.getGradedItemsByCourseAndType(courseName, type);
        //put the course and type in a list and loop to see if its empty if -y then err

        if (items.isEmpty()) {
            System.out.println("No " + type + "s found.");
            return;
        }

        //loop over the item user choose eg- quiz and list them, uses the same number logic as course

        System.out.println("Select item:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i+1) + ". " + items.get(i));
        }

        //ini input from user
        System.out.print("Choose number: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        //check for input err 
        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid choice.");
            return;
        }


        //get the item name i.e. Assignment_2

        String itemName = items.get(itemIndex);


        //ask for grade and save the variable

        System.out.print("Enter grade (0-100): ");
        double grade = scanner.nextDouble();
        scanner.nextLine();

        //put the grade varible in the database 

        db.updateGrade(courseName, type, itemName, grade);
        System.out.println("Grade updated."); //feedback
    }


    //show grades with overview, assingment marks and quizes 

private static void showGrades() {
    String courseName = selectCourse(); //helper for coruse listing 
    if (courseName == null) return;

    //set up the texts user will see 

    System.out.println("\n1. Overview");
    System.out.println("2. Assignments");
    System.out.println("3. Quizzes");
    System.out.print("Choose (1-3): ");
    int option = scanner.nextInt();
    scanner.nextLine();

    //assing the option into the string variable 
    String reportType;
    if (option == 1) {
        reportType = "Overview";
    } else if (option == 2) {
        reportType = "Assignments";
    } else {
        reportType = "Quizzes";
    }

    //put the report into the database and print to show the report 
    String report = db.getGradesReport(courseName, reportType);
    System.out.println("\n" + report);

}
}
