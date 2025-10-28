package seedu.address.model.classroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.address.model.Attendance;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for {@link ClassSession}.
 */
public class ClassSessionTest {

    private Tutor tutor;
    private TuitionClass parentClass;
    private Student alice;
    private Student bob;

    @BeforeEach
    void setUp() {
        // Reusable test data
        Set<Tag> emptyTags = new HashSet<>();

        tutor = new Tutor(
                new Name("Mr Smith"),
                new Phone("91234567"),
                new Email("smith@example.com"),
                new Address("1 Tutor Lane"),
                emptyTags);

        alice = new Student(
                new Name("Alice Tan"),
                new Phone("98765432"),
                new Email("alice@example.com"),
                new Address("10 Kent Ridge Road"),
                emptyTags);

        bob = new Student(
                new Name("Bob Lim"),
                new Phone("92345678"),
                new Email("bob@example.com"),
                new Address("20 Clementi Ave"),
                emptyTags);

        parentClass = new TuitionClass(new ClassName("CS2103T T12"), tutor);
        parentClass.addStudent(alice);
        parentClass.addStudent(bob);
    }

    @Test
    @DisplayName("Get and set session name, date/time, and location")
    void gettersSetters_workCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 10, 15, 14, 0);
        ClassSession session = new ClassSession(parentClass, "Week 5 Tutorial", time, "COM1-B201");

        // getParentClass
        assertEquals(parentClass, session.getParentClass());

        // getSessionName / setSessionName
        assertEquals("Week 5 Tutorial", session.getSessionName());
        session.setSessionName("Updated Week 5 Tutorial");
        assertEquals("Updated Week 5 Tutorial", session.getSessionName());

        // getDateTime / setDateTime
        assertEquals(time, session.getDateTime());
        LocalDateTime newTime = time.plusHours(2);
        session.setDateTime(newTime);
        assertEquals(newTime, session.getDateTime());

        // getLocation / setLocation
        assertEquals("COM1-B201", session.getLocation());
        session.setLocation("COM1-B202");
        assertEquals("COM1-B202", session.getLocation());
    }

    @Test
    @DisplayName("Constructor initializes attendance for all enrolled students")
    void constructor_initializesAttendance() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 3 Tutorial",
                LocalDateTime.of(2025, 10, 13, 10, 0),
                "COM1-B103");

        Map<Student, Attendance> attendance = session.getAttendanceRecord();

        assertEquals(2, attendance.size());
        assertFalse(attendance.get(alice).isPresent());
        assertEquals(null, attendance.get(alice).getTimestamp());
        assertFalse(attendance.get(bob).isPresent());
        assertEquals(null, attendance.get(bob).getTimestamp());
    }

    @Test
    @DisplayName("Marking attendance updates status correctly")
    void markAttendance_worksCorrectly() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 3 Tutorial",
                LocalDateTime.now(),
                "COM1-B103");

        session.markPresent(alice);
        session.markAbsent(bob);

        assertTrue(session.hasAttended(alice));
        assertFalse(session.hasAttended(bob));
        assertEquals(1, session.getAttendanceCount());
    }

    @Test
    @DisplayName("initializeAttendance adds new students but preserves existing data")
    void initializeAttendance_preservesExistingEntries() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 3 Tutorial",
                LocalDateTime.now(),
                "COM1-B103");

        session.markPresent(alice);

        // Add a new student and reinitialize
        Student charlie = new Student(
                new Name("Charlie Goh"),
                new Phone("99887766"),
                new Email("charlie@example.com"),
                new Address("30 Science Drive"),
                new HashSet<>());

        parentClass.addStudent(charlie);
        session.initializeAttendance();

        Map<Student, Attendance> attendance = session.getAttendanceRecord();

        assertTrue(attendance.containsKey(charlie));
        assertFalse(attendance.get(charlie).isPresent()); // default false
        assertEquals(null, attendance.get(charlie).getTimestamp());
        assertTrue(attendance.get(alice).isPresent());
        assertNotEquals(null, attendance.get(alice).getTimestamp());
    }

    @DisplayName("Sessions with same class, name, and time are equal")
    void equals_sameData_returnsTrue() {
        LocalDateTime time = LocalDateTime.of(2025, 10, 13, 10, 0);
        ClassSession s1 = new ClassSession(parentClass, "Week 3", time, "COM1");
        ClassSession s2 = new ClassSession(parentClass, "Week 3", time, "COM1");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    @DisplayName("Sessions with different times are not equal")
    void equals_differentTime_returnsFalse() {
        ClassSession s1 = new ClassSession(parentClass, "Week 3", LocalDateTime.now(), "COM1");
        ClassSession s2 = new ClassSession(parentClass, "Week 3", LocalDateTime.now().plusDays(1), "COM1");

        assertNotEquals(s1, s2);
    }

    @Test
    @DisplayName("toString() produces human-readable summary")
    void toString_containsSessionDetails() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 3 Tutorial",
                LocalDateTime.of(2025, 10, 13, 10, 0),
                "COM1-B103");

        String summary = session.toString();

        assertTrue(summary.contains("Week 3 Tutorial"));
        assertTrue(summary.contains("COM1-B103"));
        assertTrue(summary.contains("(0/2 present)")); // summary shown because attendance initialized in constructor
    }

    @Test
    @DisplayName("Throws exception when constructed with null parent class")
    void constructor_nullParent_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            new ClassSession(null, "Week 1", LocalDateTime.now(), "COM1"));
    }

    @Test
    @DisplayName("toString omits location when null")
    void toString_omitsLocation_whenNull() {
        ClassSession s = new ClassSession(parentClass, "Lesson 3", LocalDateTime.now(), null);
        s.initializeAttendance();
        String result = s.toString();
        assertFalse(result.contains("@"), "Expected no '@' when location is null");
    }

    @Test
    @DisplayName("toString omits attendance summary when record empty")
    void toString_omitsAttendanceSummary_whenEmptyRecord() {
        ClassSession s = new ClassSession(parentClass, "Lesson 4", LocalDateTime.now(), "COM1");
        // Intentionally clear the record to simulate "empty"
        s.getAttendanceRecord().clear();
        String result = s.toString();
        // Our ClassSession prints attendance summary ONLY when record is non-empty
        assertFalse(result.contains("present"), "Expected no attendance summary for empty attendanceRecord");
    }

    @Test
    @DisplayName("equals returns true for same object instance")
    void equals_sameInstance_returnsTrue() {
        ClassSession s = new ClassSession(parentClass, "Lesson X", LocalDateTime.now(), "COM1");
        assertTrue(s.equals(s));
    }

    @Test
    @DisplayName("equals returns false for null object")
    void equals_null_returnsFalse() {
        ClassSession s = new ClassSession(parentClass, "Lesson X", LocalDateTime.now(), "COM1");
        assertFalse(s.equals(null));
    }

    @Test
    @DisplayName("equals returns false for different object type")
    void equals_differentType_returnsFalse() {
        ClassSession s = new ClassSession(parentClass, "Lesson X", LocalDateTime.now(), "COM1");
        assertFalse(s.equals("Not a session"));
    }

    @Test
    @DisplayName("equals returns false for different parent class")
    void equals_differentParent_returnsFalse() {
        TuitionClass anotherClass = new TuitionClass(new ClassName("CS2103T T13"), tutor);
        ClassSession s1 = new ClassSession(parentClass, "Lesson X",
                LocalDateTime.of(2025, 10, 20, 9, 0), "COM1");
        ClassSession s2 = new ClassSession(anotherClass, "Lesson X",
                LocalDateTime.of(2025, 10, 20, 9, 0), "COM1");
        assertNotEquals(s1, s2);
    }

    @Test
    @DisplayName("equals returns false for different session name")
    void equals_differentName_returnsFalse() {
        ClassSession s1 = new ClassSession(parentClass, "Lesson A",
                LocalDateTime.of(2025, 10, 21, 9, 0), "COM1");
        ClassSession s2 = new ClassSession(parentClass, "Lesson B",
                LocalDateTime.of(2025, 10, 21, 9, 0), "COM1");
        assertNotEquals(s1, s2);
    }

    @Test
    @DisplayName("addSession throws exception for duplicate session name")
    void addSession_duplicateSessionName_throwsException() {
        // Add first session via addSession method
        parentClass.addSession("Week 1 Tutorial", LocalDateTime.now(), "COM1");

        // Try to add session with same name
        assertThrows(IllegalArgumentException.class, () ->
            parentClass.addSession("Week 1 Tutorial", LocalDateTime.now().plusDays(1), "COM1"));
    }

    @Test
    @DisplayName("addSession throws exception for duplicate session name with whitespace")
    void addSession_duplicateSessionNameWithWhitespace_throwsException() {
        // Add first session
        parentClass.addSession("Week 1 Tutorial", LocalDateTime.now(), "COM1");

        // Try to add session with same name but extra whitespace
        assertThrows(IllegalArgumentException.class, () ->
            parentClass.addSession("  Week 1 Tutorial  ", LocalDateTime.now().plusDays(1), "COM1"));
    }

    @Test
    @DisplayName("addSession allows different session names")
    void addSession_differentSessionNames_success() {
        parentClass.addSession("Week 1 Tutorial", LocalDateTime.now(), "COM1");
        parentClass.addSession("Week 2 Tutorial", LocalDateTime.now().plusDays(7), "COM1");
        parentClass.addSession("Week 3 Tutorial", LocalDateTime.now().plusDays(14), "COM1");

        assertEquals(3, parentClass.getAllSessions().size());
    }

    @Test
    @DisplayName("addSession enforces case-insensitive session names")
    void addSession_caseInsensitiveNames_rejectsDuplicates() {
        parentClass.addSession("Tutorial", LocalDateTime.now(), "COM1");
        // Should reject different case as they are treated as the same name
        assertThrows(IllegalArgumentException.class, () ->
            parentClass.addSession("tutorial", LocalDateTime.now().plusDays(1), "COM1"));
        assertThrows(IllegalArgumentException.class, () ->
            parentClass.addSession("TUTORIAL", LocalDateTime.now().plusDays(1), "COM1"));
    }

    @Test
    @DisplayName("getSessionDetails returns formatted details")
    void getSessionDetails_returnsFormattedDetails() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        String details = session.getSessionDetails();

        assertTrue(details.contains("Session: Week 1 Tutorial"));
        assertTrue(details.contains("Date/Time: 2024-03-15 14:30"));
        assertTrue(details.contains("Location: COM1-B103"));
        assertTrue(details.contains("Attendance:"));
        assertTrue(details.contains("Present:"));
        assertTrue(details.contains("Absent:"));
    }

    @Test
    @DisplayName("getSessionDetails omits location when null")
    void getSessionDetails_omitsLocationWhenNull() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                null);

        String details = session.getSessionDetails();

        assertTrue(details.contains("Session: Week 1 Tutorial"));
        assertTrue(details.contains("Date/Time: 2024-03-15 14:30"));
        assertFalse(details.contains("Location:"));
    }

    @Test
    @DisplayName("getSessionDetails shows correct attendance count")
    void getSessionDetails_showsCorrectAttendanceCount() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        session.markPresent(alice);
        session.markAbsent(bob);

        String details = session.getSessionDetails();

        assertTrue(details.contains("Attendance: 1/2 present"));
    }

    @Test
    @DisplayName("getSessionDetails shows None when no present students")
    void getSessionDetails_showsNoneWhenNoPresentStudents() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        String details = session.getSessionDetails();

        assertTrue(details.contains("Present:" + System.lineSeparator() + "None"));
    }

    @Test
    @DisplayName("getSessionDetails shows None when no absent students")
    void getSessionDetails_showsNoneWhenNoAbsentStudents() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        session.markPresent(alice);
        session.markPresent(bob);

        String details = session.getSessionDetails();

        assertTrue(details.contains("Absent:" + System.lineSeparator() + "None"));
    }

    @Test
    @DisplayName("getSessionDetails handles null student gracefully")
    void getSessionDetails_handlesNullStudent() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        // Manually add a null entry to simulate edge case
        session.getAttendanceRecord().put(null, new Attendance(true, LocalDateTime.now()));

        String details = session.getSessionDetails();

        assertTrue(details.contains("Unknown student"));
    }

    @Test
    @DisplayName("toString with location shows @ symbol")
    void toString_withLocation_showsAtSymbol() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        String result = session.toString();

        assertTrue(result.contains("@"));
        assertTrue(result.contains("COM1-B103"));
    }

    @Test
    @DisplayName("getAttendanceCount returns correct count for multiple students")
    void getAttendanceCount_multipleStudents_returnsCorrectCount() {
        ClassSession session = new ClassSession(
                parentClass,
                "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30),
                "COM1-B103");

        assertEquals(0, session.getAttendanceCount());

        session.markPresent(alice);
        assertEquals(1, session.getAttendanceCount());

        session.markPresent(bob);
        assertEquals(2, session.getAttendanceCount());

        session.markAbsent(alice);
        assertEquals(1, session.getAttendanceCount());
    }
}
