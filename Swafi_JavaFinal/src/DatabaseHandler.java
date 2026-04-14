import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:courses.db"; //tells the driver to use sqlite

    public DatabaseHandler() {
        createTables(); //create table
    }

    private void createTables() {
        String courses = "CREATE TABLE IF NOT EXISTS courses (id INTEGER PRIMARY KEY, name TEXT NOT NULL, instructor TEXT)";
        String items = "CREATE TABLE IF NOT EXISTS graded_items (id INTEGER PRIMARY KEY, course_name TEXT, type TEXT, name TEXT, due_date TEXT, grade REAL DEFAULT 0.0, completed BOOLEAN DEFAULT 0)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(courses);
            stmt.executeUpdate(items);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public void addCourse(Course course) {
    String sql = "INSERT INTO courses(name, instructor) VALUES(?,?)";
    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, course.getName());      
        pstmt.setString(2, course.getInstructor()); 
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Error adding course: " + e.getMessage());
    }
}

    public boolean addGradedItem(String courseName, GradedItem item) {
    String sql = "INSERT INTO graded_items(course_name, type, name, due_date) VALUES(?,?,?,?)";
    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, courseName);
        pstmt.setString(2, item.getType());   
        pstmt.setString(3, item.getName());   
        pstmt.setString(4, item.getDueDate()); 
        pstmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        System.err.println("Error adding graded item: " + e.getMessage());
        return false;
    }
}

    public List<String> getAllCourses() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name, instructor FROM courses";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("name") + " - " + rs.getString("instructor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getGradedItemsByCourseAndType(String courseName, String type) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT name FROM graded_items WHERE course_name = ? AND type = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setString(2, type);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateGrade(String courseName, String type, String itemName, double grade) {
        String sql = "UPDATE graded_items SET grade = ?, completed = 1 WHERE course_name = ? AND type = ? AND name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, grade);
            pstmt.setString(2, courseName);
            pstmt.setString(3, type);
            pstmt.setString(4, itemName);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getGradesReport(String courseName, String option) {
        if (option.equals("Overview")) {
            return getOverview(courseName);
        } else if (option.equals("Assignments")) {
            return getItemsReport(courseName, "Assignment");
        } else if (option.equals("Quizzes")) {
            return getItemsReport(courseName, "Quiz");
        }
        return "Invalid option";
    }

    private String getOverview(String courseName) {
        StringBuilder sb = new StringBuilder("Overview for " + courseName + "\n\n");
        sb.append(getItemsReport(courseName, "Assignment")).append("\n\n");
        sb.append(getItemsReport(courseName, "Quiz"));
        return sb.toString();
    }

    private String getItemsReport(String courseName, String type) {
        StringBuilder sb = new StringBuilder(type + "s:\n");
        String sql = "SELECT name, grade FROM graded_items WHERE course_name = ? AND type = ?";
        boolean hasItems = false;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseName);
            pstmt.setString(2, type);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                hasItems = true;
                String name = rs.getString("name");
                double grade = rs.getDouble("grade");
                sb.append("• ").append(name).append(" - ").append(grade).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!hasItems) {
            sb.append("No ").append(type).append("s added yet.");
        }
        return sb.toString();
    }
}
