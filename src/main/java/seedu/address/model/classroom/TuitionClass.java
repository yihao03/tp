package seedu.address.model.classroom;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Represents a tuition class identified by its {@link ClassName}.
 */
public class TuitionClass {
    private final ClassName name;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Tutor> tutors = new ArrayList<>();

    /**
     * Creates a {@code TuitionClass} with the given name.
     * @param name non-null class name
     */
    public TuitionClass(ClassName name) {
        requireNonNull(name);
        this.name = name;
    }

    public ClassName getName() {
        return name;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Tutor> getTutors() {
        return tutors;
    }

    /**
     * Adds a student to this tuition class if not already present.
     */
    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            student.addClass(this);
        }
    }

    /**
     * Adds a tutor to this tuition class if not already present.
     */
    public void addTutor(Tutor tutor) {
        if (!tutors.contains(tutor)) {
            tutors.add(tutor);
            tutor.addClass(this);
        }
    }

    /**
     * Removes a student from this tuition class.
     * @throws PersonNotFoundException if student is not in this class
     */
    public void removeStudent(Student student) {
        if (!students.remove(student)) {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Removes a tutor from this tuition class.
     * @throws PersonNotFoundException if tutor is not in this class
     */
    public void removeTutor(Tutor tutor) {
        if (!tutors.remove(tutor)) {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Replaces the target student with the edited student.
     * @throws PersonNotFoundException if target student is not in this class
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(target);
        requireNonNull(editedStudent);

        int index = students.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        students.set(index, editedStudent);
    }

    /**
     * Replaces the target tutor with the edited tutor.
     * @throws PersonNotFoundException if target tutor is not in this class
     */
    public void setTutor(Tutor target, Tutor editedTutor) {
        requireNonNull(target);
        requireNonNull(editedTutor);

        int index = tutors.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        tutors.set(index, editedTutor);
    }

    /** Identity check: same class name means same class. */
    public boolean isSameClass(TuitionClass other) {
        return other != null && name.equals(other.name);
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof TuitionClass && name.equals(((TuitionClass) o).name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
