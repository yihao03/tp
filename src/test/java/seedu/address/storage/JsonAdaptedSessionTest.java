package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.ClassSession;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;

public class JsonAdaptedSessionTest {

    private static final String VALID_SESSION_NAME = "Week 1";
    private static final String VALID_DATETIME = "2024-10-28T14:00:00";
    private static final String VALID_LOCATION = "Room 101";

    @Test
    public void constructor_withAttendanceData_success() throws Exception {
        // Create a tuition class with students
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        Student alice = new Student(new Name("Alice"), new Phone("91234567"),
                new Email("alice@example.com"), new Address("123 Street"), new java.util.HashSet<>());
        Student bob = new Student(new Name("Bob"), new Phone("81234567"),
                new Email("bob@example.com"), new Address("456 Street"), new java.util.HashSet<>());

        tuitionClass.addStudent(alice);
        tuitionClass.addStudent(bob);

        // Create session and mark attendance
        tuitionClass.addSession(VALID_SESSION_NAME, LocalDateTime.parse(VALID_DATETIME), VALID_LOCATION);
        ClassSession session = tuitionClass.getAllSessions().get(0);
        session.markPresent(alice);
        session.markAbsent(bob);

        // Convert to JSON
        JsonAdaptedSession jsonSession = new JsonAdaptedSession(session);

        // Verify attendance lists
        assertEquals(1, jsonSession.getPresentStudents().size());
        assertTrue(jsonSession.getPresentStudents().stream().anyMatch(list -> list.get(0).equals("Alice")));
        assertEquals(1, jsonSession.getAbsentStudents().size());
        assertTrue(jsonSession.getAbsentStudents().stream().anyMatch(list -> list.get(0).equals("Bob")));
    }

    @Test
    public void constructor_withNullLists_defaultsToEmptyLists() {
        JsonAdaptedSession jsonSession = new JsonAdaptedSession(
                VALID_SESSION_NAME, VALID_DATETIME, VALID_LOCATION, null, null);

        assertEquals(0, jsonSession.getPresentStudents().size());
        assertEquals(0, jsonSession.getAbsentStudents().size());
    }

    public void constructor_withValidLists_success() {
        List<List<String>> presentStudents = Arrays.asList(
                Arrays.asList("Alice", VALID_DATETIME),
                Arrays.asList("Bob", VALID_DATETIME));
        List<List<String>> absentStudents = Arrays.asList(
                Arrays.asList("Charlie", VALID_DATETIME));

        JsonAdaptedSession jsonSession = new JsonAdaptedSession(
                VALID_SESSION_NAME, VALID_DATETIME, VALID_LOCATION, presentStudents, absentStudents);

        assertEquals(2, jsonSession.getPresentStudents().size());
        assertEquals(1, jsonSession.getAbsentStudents().size());
        assertTrue(jsonSession.getPresentStudents().stream().anyMatch(list -> list.get(0).equals("Alice")));
        assertTrue(jsonSession.getAbsentStudents().stream().anyMatch(list -> list.get(0).equals("Bob")));
    }

    @Test
    public void getters_success() {
        List<List<String>> presentStudents = Arrays.asList(Arrays.asList("Alice", VALID_DATETIME));
        List<List<String>> absentStudents = Arrays.asList(Arrays.asList("Bob", VALID_DATETIME));

        JsonAdaptedSession jsonSession = new JsonAdaptedSession(
                VALID_SESSION_NAME, VALID_DATETIME, VALID_LOCATION, presentStudents, absentStudents);

        assertEquals(VALID_SESSION_NAME, jsonSession.getSessionName());
        assertEquals(VALID_DATETIME, jsonSession.getDateTime());
        assertEquals(VALID_LOCATION, jsonSession.getLocation());
        assertEquals(presentStudents, jsonSession.getPresentStudents());
        assertEquals(absentStudents, jsonSession.getAbsentStudents());
    }
}
