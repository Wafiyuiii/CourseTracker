import java.util.Scanner;
import java.util.List;

public class Main { //create a new database object 
    private static DatabaseHandler db = new DatabaseHandler();
    private static Scanner scanner = new Scanner(System.in);


    //basic intro stuff gui/terminalText for the user to see 
    public static void main(String[] args) {

        while (true) {
            // show menu every time
            System.out.println("========= Course Tracker =========");
            System.out.println("1. Add New Course");
            System.out.println("2. Add Assignment");
            System.out.println("3. Add Quiz");
            System.out.println("4. Update Grade");
            System.out.println("5. Show Grades");
            System.out.println("6. Exit");
            System.out.print("Choose (1-6): ");

            int input = scanner.nextInt();

            //refer to the correct methods based on users input 

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
                return; // exits the program
            } else {
                System.out.println("Invalid option, pick 1-6.");
            }
        }
    }


/*Add course mehtod
take the name of the course and isntructor from the user 
add that to the databases add course so later it can use the input data to create nessasry sql commands and/or tables */
    private static void addCourse() {
        System.out.print("Course Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Instructor: ");
        String instructor = scanner.nextLine().trim();

        db.addCourse(name, instructor);
        System.out.println("Course added.");
    }


    //method for adding assingmnents--> store a list in it call the database and store the course info 
    private static void addAssignment() {
        List<String> courses = db.getAllCourses();
        
        //check if course is empty if yes then show warning and go back to main gui
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }


        /*bacaily we want to show the coruse as a 1,2,3.. list so we loop over our list of coruses and print it we do i+1 because arrays starts from 0 
        it wont be correct to show 0.coruse 1 */
        
        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));

        //ask user for input to choose a course and show fail if they choose something that is not in the list like if its less than 0 or a number more than the list size 

        }
        System.out.print("Choose number: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();
        if (index < 0 || index >= courses.size()) {
            System.out.println("Invalid choice.");
            return;


        }
        String courseName = courses.get(index).split(" - ")[0]; // i do not get this what is courses.get(index)


        //ask for assignment name and also due date and store the input 
        System.out.print("Assignment Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = scanner.nextLine().trim();

        //finally add the name and due date input from user to the database's GradedItem 
        db.addGradedItem(courseName, "Assignment", name, dueDate);
        System.out.println("Assignment added."); //show success feedback

    }


    //same logic as addAssignment with slight variable name changes 
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

        if (courses.isEmpty()) {//check if any course are there
            System.out.println("No courses yet.");
            return;
        }

        System.out.println("Select course:"); //same logic as before to choose a certain course
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i+1) + ". " + courses.get(i));
        }
        System.out.print("Choose number: "); //again sam e logic as before 
        int courseIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        if (courseIndex < 0 || courseIndex >= courses.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String courseName = courses.get(courseIndex).split(" - ")[0];

        System.out.print("Type (Assignment or Quiz): "); //ask for quiz or assignment what they want to grade
        String type = scanner.nextLine().trim();

        List<String> items = db.getGradedItemsByCourseAndType(courseName, type);
        //create a list items, and call the database fetch the data from type , which contains course name and the type such as quiz or assingment 
        //check if the list is empty i.e if any items are there or not
        if (items.isEmpty()) {
            System.out.println("No " + type + "s found.");
            return;
        }

        System.out.println("Select item:"); //show the list of the items same logic as before to show course list  and ask for input to what to grade 
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i+1) + ". " + items.get(i));
        }
        System.out.print("Choose number: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        //same logic to check if user input valid number

        if (itemIndex < 0 || itemIndex >= items.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        //get that particular item 

        String itemName = items.get(itemIndex);

        //store the grade user inputs 
        System.out.print("Enter grade (0-100): ");
        double grade = scanner.nextDouble();
        scanner.nextLine();

        //add the grade to the UpdateGrade calling the database so it will show cName, type, type number (like quiz 2 or 3 ) and its grade 
        db.updateGrade(courseName, type, itemName, grade)) 
            System.out.println("Grade updated."); //feed back
        
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
