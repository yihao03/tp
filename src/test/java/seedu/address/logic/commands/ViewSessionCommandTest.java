package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;

/**
 * Unit tests for {@link ViewSessionCommand}.
 */
public class ViewSessionCommandTest {

    private Model model;
    private TuitionClass mathClass;
    private TuitionClass scienceClass;
    private Student student;

    @BeforeEach
    void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a student for session initialization
        student = new Student(
                new Name("Alice Tan"),
                new Phone("98765432"),
                new Email("alice@example.com"),
                new Address("10 Kent Ridge Road"),
                new HashSet<>());
        model.addPerson(student);

        // Create math class with sessions
        mathClass = new TuitionClass(new ClassName("Math101"));
        model.addClass(mathClass);
        model.addStudentToClass(student, mathClass);
        mathClass.addSession("Week 1 Tutorial", LocalDateTime.of(2024, 3, 15, 14, 30), "COM1-B103");
        mathClass.addSession("Week 2 Tutorial", LocalDateTime.of(2024, 3, 22, 14, 30), "COM1-B104");

        // Create science class with one session
        scienceClass = new TuitionClass(new ClassName("Science101"));
        model.addClass(scienceClass);
        model.addStudentToClass(student, scienceClass);
        scienceClass.addSession("Lab Session", LocalDateTime.of(2024, 3, 20, 10, 0), "Lab A");
    }

    @Test
    @DisplayName("Constructor throws exception when className is null")
    void constructor_nullClassName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewSessionCommand(null, "Week 1 Tutorial"));
    }

    @Test
    @DisplayName("Constructor throws exception when sessionName is null")
    void constructor_nullSessionName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewSessionCommand("Math101", null));
    }

    @Test
    @DisplayName("Execute throws exception when model is null")
    void execute_nullModel_throwsNullPointerException() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    @DisplayName("Execute views session details successfully")
    void execute_validSession_success() throws CommandException {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Week 1 Tutorial"));
        assertTrue(feedback.contains("2024-03-15"));
        assertTrue(feedback.contains("COM1-B103"));
    }

    @Test
    @DisplayName("Execute views session with different case session name")
    void execute_caseInsensitiveSessionName_success() throws CommandException {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "week 1 tutorial");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Week 1 Tutorial"));
    }

    @Test
    @DisplayName("Execute views session with whitespace in session name")
    void execute_sessionNameWithWhitespace_success() throws CommandException {
        ViewSessionCommand command = new ViewSessionCommand("Math101", " Week 1 Tutorial ");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Week 1 Tutorial"));
    }

    @Test
    @DisplayName("Execute views session without location")
    void execute_sessionWithoutLocation_success() throws CommandException {
        // Add a session without location
        mathClass.addSession("Assignment Review", LocalDateTime.of(2024, 3, 25, 15, 0), null);

        ViewSessionCommand command = new ViewSessionCommand("Math101", "Assignment Review");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Assignment Review"));
    }

    @Test
    @DisplayName("Execute throws exception when class not found")
    void execute_classNotFound_throwsCommandException() {
        ViewSessionCommand command = new ViewSessionCommand("NonExistentClass", "Week 1 Tutorial");

        assertThrows(CommandException.class, ViewSessionCommand.MESSAGE_CLASS_NOT_FOUND, () ->
                command.execute(model));
    }

    @Test
    @DisplayName("Execute throws exception when session not found")
    void execute_sessionNotFound_throwsCommandException() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "NonExistentSession");

        assertThrows(CommandException.class, ViewSessionCommand.MESSAGE_SESSION_NOT_FOUND, () ->
                command.execute(model));
    }

    @Test
    @DisplayName("Execute shows attendance information")
    void execute_displaysAttendanceInformation() throws CommandException {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Attendance:"));
        assertTrue(feedback.contains("Present:"));
        assertTrue(feedback.contains("Absent:"));
    }

    @Test
    @DisplayName("Execute views second session successfully")
    void execute_secondSession_success() throws CommandException {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 2 Tutorial");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Week 2 Tutorial"));
        assertTrue(feedback.contains("2024-03-22"));
        assertTrue(feedback.contains("COM1-B104"));
    }

    @Test
    @DisplayName("Execute views session from different class")
    void execute_differentClass_success() throws CommandException {
        ViewSessionCommand command = new ViewSessionCommand("Science101", "Lab Session");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Lab Session"));
        assertTrue(feedback.contains("Lab A"));
    }

    @Test
    @DisplayName("Equals returns true for same object instance")
    void equals_sameInstance_returnsTrue() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        assertTrue(command.equals(command));
    }

    @Test
    @DisplayName("Equals returns true for same className and sessionName")
    void equals_sameValues_returnsTrue() {
        ViewSessionCommand command1 = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        ViewSessionCommand command2 = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        assertTrue(command1.equals(command2));
    }

    @Test
    @DisplayName("Equals returns false for different className")
    void equals_differentClassName_returnsFalse() {
        ViewSessionCommand command1 = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        ViewSessionCommand command2 = new ViewSessionCommand("Science101", "Week 1 Tutorial");
        assertFalse(command1.equals(command2));
    }

    @Test
    @DisplayName("Equals returns false for different sessionName")
    void equals_differentSessionName_returnsFalse() {
        ViewSessionCommand command1 = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        ViewSessionCommand command2 = new ViewSessionCommand("Math101", "Week 2 Tutorial");
        assertFalse(command1.equals(command2));
    }

    @Test
    @DisplayName("Equals returns false for null")
    void equals_null_returnsFalse() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        assertFalse(command.equals(null));
    }

    @Test
    @DisplayName("Equals returns false for different type")
    void equals_differentType_returnsFalse() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        assertFalse(command.equals("Not a command"));
    }

    @Test
    @DisplayName("Equals returns false for different command type")
    void equals_differentCommandType_returnsFalse() {
        ViewSessionCommand command1 = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        AddSessionCommand command2 = new AddSessionCommand("Math101", "Week 1 Tutorial",
                LocalDateTime.of(2024, 3, 15, 14, 30), "COM1-B103");
        assertFalse(command1.equals(command2));
    }

    @Test
    @DisplayName("ToString contains className and sessionName")
    void toString_containsClassNameAndSessionName() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "Week 1 Tutorial");
        String result = command.toString();

        assertTrue(result.contains("ViewSessionCommand"));
        assertTrue(result.contains("Math101"));
        assertTrue(result.contains("Week 1 Tutorial"));
    }

    @Test
    @DisplayName("Execute views empty session name")
    void execute_emptySessionName_throwsCommandException() {
        ViewSessionCommand command = new ViewSessionCommand("Math101", "");
        assertThrows(CommandException.class, ViewSessionCommand.MESSAGE_SESSION_NOT_FOUND, () ->
                command.execute(model));
    }

    @Test
    @DisplayName("Execute handles multiple sessions in class")
    void execute_multipleSessionsInClass_success() throws CommandException {
        // Add more sessions
        mathClass.addSession("Midterm", LocalDateTime.of(2024, 4, 15, 14, 0), "LT27");
        mathClass.addSession("Final", LocalDateTime.of(2024, 5, 15, 14, 0), "LT28");

        ViewSessionCommand command = new ViewSessionCommand("Math101", "Midterm");
        CommandResult result = command.execute(model);

        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Session details:"));
        assertTrue(feedback.contains("Midterm"));
        assertTrue(feedback.contains("LT27"));
    }
}

