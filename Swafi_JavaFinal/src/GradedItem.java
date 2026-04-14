public abstract class GradedItem { //abstract calss

    protected String name;
    protected String dueDate;
    protected double grade = 0.0;
    protected boolean completed = false;

    public GradedItem(String name, String dueDate) { //the constructor we will call for the items
        this.name = name;
        this.dueDate = dueDate;
    }

    public abstract String getType(); //abstract mehtod the child must implement 

    public void setGrade(double grade) { //check true for setting grade 
        this.grade = grade;
        this.completed = true;
    }

    public String getName() { return name; }
    public String getDueDate() { return dueDate; }
    public double getGrade() { return grade; }
    public boolean isCompleted() { return completed; }

    // Simple method to show info
    public String getInfo() {
        return name + " (" + dueDate + ") - Grade: " + grade;
    }
}
