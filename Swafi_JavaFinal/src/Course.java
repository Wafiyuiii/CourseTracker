import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private String instructor;
    private List<GradedItem> gradedItems = new ArrayList<>();

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

    public void addGradedItem(GradedItem item) {
        gradedItems.add(item);
    }

    public String getName() { return name; }
    public String getInstructor() { return instructor; }
    public List<GradedItem> getGradedItems() { return gradedItems; }
}
