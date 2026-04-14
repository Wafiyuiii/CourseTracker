//child of GradedItem

public class Assignment extends GradedItem {
    
    public Assignment(String name, String dueDate) {
       //call the parent constructor 
        super(name, dueDate);
    }

    @Override
    public String getType() {
        return "Assignment";
    }
}

