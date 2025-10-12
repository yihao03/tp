package seedu.address.model.classes;

import java.util.ArrayList;
import java.util.Objects;

import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;

/**
 * Represents a class, consisting of a tutor, a class name, and a roster of
 * students. Stores a mutable list of students enrolled under the given tutor.
 */
public class Class {
    /** Tutor responsible for this class. */
    private Tutor tutor;

    /** Roster of students in this class. */
    private ArrayList<Student> students;

    /** Human-readable name of the class. */
    private String className;

    /**
     * Constructs a class with the specified tutor and name.
     *
     * @param tutor the tutor in charge of the class
     * @param className the human-readable class name (e.g., "CS2103T T12")
     */
    public Class(Tutor tutor, String className) {
        this.tutor = tutor;
        this.students = new ArrayList<>();
        this.className = className;
    }

    /**
     * Adds a student to this class's roster.
     *
     * @param student the student to add
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Returns the name of this class.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Returns the tutor in charge of this class.
     *
     * @return the tutor
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Returns the list of students enrolled in this class. Note: The returned
     * list is the internal mutable list; modifications affect this instance.
     *
     * @return the list of students
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * Returns true if both classes have the same name.
     * This defines a weaker notion of equality between two classes.
     */
    public boolean isSameClass(Class otherClass) {
        if (otherClass == this) {
            return true;
        }

        return otherClass != null && otherClass.getClassName().equals(getClassName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Class)) {
            return false;
        }

        Class otherClass = (Class) other;
        return className.equals(otherClass.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String toString() {
        return className;
    }
}
