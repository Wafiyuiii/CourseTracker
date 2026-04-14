public abstract class GradedItem {
    protected String name;
    protected String dueDate;
    protected double grade = 0.0;
    protected boolean completed = false;

    public GradedItem(String name, String dueDate) {
        this.name = name;
        this.dueDate = dueDate;
    }

    public abstract String getType();

    public void setGrade(double grade) {
        this.grade = grade;
        this.completed = true;
    }

    public String getName() { return name; }
    public String getDueDate() { return dueDate; }
    public double getGrade() { return grade; }
    public boolean isCompleted() { return completed; }
}
