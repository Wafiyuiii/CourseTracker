public class Assignment extends GradedItem {
    public Assignment(String name, String dueDate) {
        super(name, dueDate);
    }

    @Override
    public String getType() {
        return "Assignment";
    }
}
