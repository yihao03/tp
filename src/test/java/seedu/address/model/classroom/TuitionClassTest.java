package seedu.address.model.classroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for {@link TuitionClass}.
 */
public class TuitionClassTest {

    private Tutor tutor;
    private TuitionClass tuitionClass;
    private Student alice;
    private Student bob;

    @BeforeEach
    void setUp() {
        Set<Tag> emptyTags = new HashSet<>();

        tutor = new Tutor(
                new Name("Mr Smith"),
                new Phone("91234567"),
                new Email("smith@example.com"),
                new Address("1 Tutor Lane"),
                emptyTags
        );

        tuitionClass = new TuitionClass(new ClassName("CS2103T T12"), tutor);

        alice = new Student(
                new Name("Alice Tan"),
                new Phone("98765432"),
                new Email("alice@example.com"),
                new Address("10 Kent Ridge Road"),
                emptyTags
        );

        bob = new Student(
                new Name("Bob Lim"),
                new Phone("92345678"),
                new Email("bob@example.com"),
                new Address("20 Clementi Ave"),
                emptyTags
        );
    }

    @Test
    @DisplayName("Add and remove students from class")
    void addRemoveStudents() {
        tuitionClass.addStudent(alice);
        tuitionClass.addStudent(bob);

        assertTrue(tuitionClass.getStudents().contains(alice));
        assertTrue(tuitionClass.getStudents().contains(bob));
        assertEquals(2, tuitionClass.getStudents().size());

        tuitionClass.removeStudent(alice);
        assertFalse(tuitionClass.getStudents().contains(alice));
        assertEquals(1, tuitionClass.getStudents().size());
    }

    @Test
    @DisplayName("Add and remove sessions from class")
    void addRemoveSessions() {
        tuitionClass.addStudent(alice);
        tuitionClass.addStudent(bob);

        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        LocalDateTime pastTime = LocalDateTime.now().minusDays(1);

        ClassSession session1 = tuitionClass.addSession("Future Session", futureTime, "COM1-B103");
        ClassSession session2 = tuitionClass.addSession("Past Session", pastTime, "COM1-B104");

        assertEquals(2, tuitionClass.getAllSessions().size());
        assertTrue(tuitionClass.getAllSessions().contains(session1));
        assertTrue(tuitionClass.getAllSessions().contains(session2));

        tuitionClass.removeSession(session1);
        assertEquals(1, tuitionClass.getAllSessions().size());
        assertFalse(tuitionClass.getAllSessions().contains(session1));
        assertTrue(tuitionClass.getAllSessions().contains(session2));
    }

    @Test
    @DisplayName("Retrieve future and past sessions correctly")
    void getFutureAndPastSessions() {
        tuitionClass.addStudent(alice);
        tuitionClass.addStudent(bob);

        LocalDateTime futureTime1 = LocalDateTime.now().plusDays(2);
        LocalDateTime futureTime2 = LocalDateTime.now().plusHours(3);
        LocalDateTime pastTime1 = LocalDateTime.now().minusDays(1);

        ClassSession futureSession1 = tuitionClass.addSession("Future 1", futureTime1, "COM1-B101");
        ClassSession futureSession2 = tuitionClass.addSession("Future 2", futureTime2, "COM1-B102");
        ClassSession pastSession = tuitionClass.addSession("Past 1", pastTime1, "COM1-B103");

        assertTrue(tuitionClass.getFutureSessions().contains(futureSession1));
        assertTrue(tuitionClass.getFutureSessions().contains(futureSession2));
        assertFalse(tuitionClass.getFutureSessions().contains(pastSession));

        assertTrue(tuitionClass.getPastSessions().contains(pastSession));
        assertFalse(tuitionClass.getPastSessions().contains(futureSession1));
        assertFalse(tuitionClass.getPastSessions().contains(futureSession2));
    }
}
