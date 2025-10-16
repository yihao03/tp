package seedu.address.model.classroom;

import static java.util.Objects.requireNonNull;

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

    /** Tutor responsible for this class (nullable until assigned). */
    private Tutor tutor;

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
     * @param tutor  tutor in charge (nullable)
     */
    public TuitionClass(ClassName name, Tutor tutor) {
        requireNonNull(name);
        this.name = name;
        this.tutor = tutor;
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

    @Override
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

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     * Checks if a tutor is assigned to this class.
     */
    public boolean hasTutor(Tutor tutor) {
        requireNonNull(tutor);
        return this.tutor != null && this.tutor.equals(tutor);
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
     * Add student to student list
     */
    public void addStudent(Student student) {
        requireNonNull(student);
        students.add(student);
    }

    /**
     * Checks if a student is already in the class roster.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Remove student from student list
     */
    public void removeStudent(Student student) {
        requireNonNull(student);
        students.remove(student);
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
