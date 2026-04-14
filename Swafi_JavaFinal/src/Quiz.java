public class Quiz extends GradedItem {
    public Quiz(String name, String dueDate) {
        super(name, dueDate);
    }

    @Override
    public String getType() {
        return "Quiz";
    }
}
