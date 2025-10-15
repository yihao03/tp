package seedu.address.model.classroom;

import static java.util.Objects.requireNonNull;

import seedu.address.model.person.exceptions.PersonNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;

/**
 * Represents a tuition class in the address book.
 * <p>
 * Identity is determined solely by {@link ClassName}. Additional properties such as tutor,
 * roster, and sessions do not affect identity/equality.
 */
public class TuitionClass {

    /** Canonical identity of the class. */
    private final ClassName name;

    /** Tutor(s) responsible for this class (nullable until assigned). */
    private ArrayList<Tutor> tutors = new ArrayList<>();

    /** Mutable roster of enrolled students. */
    private final ArrayList<Student> students = new ArrayList<>();

    /** Mutable list of sessions conducted under this class. */
    private final ArrayList<ClassSession> sessions = new ArrayList<>();

    /**
     * Constructs a {@code TuitionClass} with the given name.
     *
     * @param name non-null class name
     */
    public TuitionClass(ClassName name) {
        this(name, null);
    }

    /**
     * Constructs a {@code TuitionClass} with the given name and tutor.
     *
     * @param tutor2 non-null class name
     * @param tutor tutor in charge (nullable)
     */
    public TuitionClass(ClassName name, Tutor tutor) {
        requireNonNull(name);
        this.name = name;
        this.tutors.add(tutor);
    }

    // ---------------------------------------------------------------------
    // Identity
    // ---------------------------------------------------------------------

    public ClassName getName() {
        return name;
    }

          /**
     * Back-compat helper for older code/tests that used a String name.
     */
    public String getClassName() {
        return name.value;
    }

    /**
     * Returns true if both classes share the same {@link ClassName}.
     */
    public boolean isSameClass(TuitionClass other) {
        return other != null && name.equals(other.name);
    }

    public boolean equals(Object other) {
        return other == this
                || (other instanceof TuitionClass && name.equals(((TuitionClass) other).name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        String tutorStr = (tutor == null) ? "Unassigned" : tutor.getName().fullName;
        return String.format("TuitionClass{name=%s, tutor=%s, students=%d, sessions=%d}",
                name.value, tutorStr, students.size(), sessions.size());
    }

    // ---------------------------------------------------------------------
    // Tutor
    // ---------------------------------------------------------------------

    public ArrayList<Tutor> getTutors() {
        return tutors;
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
     * Removes a tutor from this tuition class.
     * @throws PersonNotFoundException if tutor is not in this class
     */
    public void removeTutor(Tutor tutor) {
        if (!tutors.remove(tutor)) {
            throw new PersonNotFoundException();
        }
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

    // ---------------------------------------------------------------------
    // Roster
    // ---------------------------------------------------------------------

    /**
     * Returns the internal mutable roster.
     */
    public ArrayList<Student> getStudents() {
        return students;
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
     * Removes a student from this tuition class.
     * @throws PersonNotFoundException if student is not in this class
     */
    public void removeStudent(Student student) {
        requireNonNull(student);
        if (!students.remove(student)) {
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

    // ---------------------------------------------------------------------
    // Sessions
    // ---------------------------------------------------------------------

    /**
     * Add session to session list and return the list
     */
    public ClassSession addSession(String sessionName, LocalDateTime dateTime, String location) {
        ClassSession session = new ClassSession(this, sessionName, dateTime, location);
        sessions.add(session);
        return session;
    }

    /**
     * Remove session from session list
     */
    public void removeSession(ClassSession session) {
        sessions.remove(session);
    }

    /**
     * Return session list
     */
    public List<ClassSession> getAllSessions() {
        return new ArrayList<>(sessions);
    }

    /**
     * Returns filtered session list that includes future sessions
     */
    public List<ClassSession> getFutureSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessions.stream()
                .filter(s -> s.getDateTime().isAfter(now))
                .collect(Collectors.toList());
    }

    /**
     * Returns filtered session list that includes past sessions
     */
    public List<ClassSession> getPastSessions() {
        LocalDateTime now = LocalDateTime.now();
        return sessions.stream()
                .filter(s -> s.getDateTime().isBefore(now))
                .collect(Collectors.toList());
    }
}
