import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private String instructor;
    private List<GradedItem> gradedItems = new ArrayList<>(); //list to hold the items 

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }

    public void addGradedItem(GradedItem item) {
        gradedItems.add(item);
        //add an item to this course
    }

    public List<GradedItem> getGradedItems() {
        return gradedItems;

        //return the graded items of this course 
    }

    public String getName() {
        return name;

        //reutrn course name
    }

    public String getInstructor() {
        return instructor;

        //reutrn instructor name
    }

    
    
}
