package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tutor;

/**
 * Unit tests for {@link ListSessionCommand}.
 */
public class ListSessionCommandTest {

    private Model model;
    private TuitionClass mathClass;
    private TuitionClass scienceClass;
    private TuitionClass emptyClass;
    private Tutor tutor;

    @BeforeEach
    void setUp() {
        model = new ModelManager();

        tutor = new Tutor(
                new Name("Mr Smith"),
                new Phone("91234567"),
                new Email("smith@example.com"),
                new Address("1 Tutor Lane"),
                new HashSet<>()
        );

        // Class with multiple sessions
        mathClass = new TuitionClass(new ClassName("Math101"), tutor);
        mathClass.addSession("Session1", LocalDateTime.of(2025, 10, 25, 14, 0), "Room 101");
        mathClass.addSession("Session2", LocalDateTime.of(2025, 11, 1, 14, 0), "Room 102");

        // Class with one session
        scienceClass = new TuitionClass(new ClassName("Science101"), tutor);
        scienceClass.addSession("Lab1", LocalDateTime.of(2025, 10, 26, 10, 0), "Lab A");

        // Class with no sessions
        emptyClass = new TuitionClass(new ClassName("EmptyClass"), tutor);

        model.addClass(mathClass);
        model.addClass(scienceClass);
        model.addClass(emptyClass);
    }

    @Test
    @DisplayName("Constructor throws exception when className is null")
    void constructor_nullClassName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListSessionCommand(null));
    }

    @Test
    @DisplayName("Execute throws exception when model is null")
    void execute_nullModel_throwsNullPointerException() {
        ListSessionCommand command = new ListSessionCommand("Math101");
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    @DisplayName("Execute lists sessions for valid class")
    void execute_validClass_success() throws CommandException {
        ListSessionCommand command = new ListSessionCommand("Math101");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("2 session(s)"));
        assertTrue(result.getFeedbackToUser().contains("Math101"));
        assertEquals(2, model.getFilteredSessionList().size());
    }

    @Test
    @DisplayName("Execute shows no sessions message for empty class")
    void execute_emptyClass_success() throws CommandException {
        ListSessionCommand command = new ListSessionCommand("EmptyClass");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("[No sessions]"));
        assertEquals(0, model.getFilteredSessionList().size());
    }

    @Test
    @DisplayName("Execute is case insensitive for class name")
    void execute_caseInsensitive_success() throws CommandException {
        ListSessionCommand command = new ListSessionCommand("math101");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("2 session(s)"));
        assertEquals(2, model.getFilteredSessionList().size());
    }

    @Test
    @DisplayName("Execute throws exception when class not found")
    void execute_classNotFound_throwsCommandException() {
        ListSessionCommand command = new ListSessionCommand("NonExistentClass");

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));

        assertTrue(exception.getMessage().contains("Class not found"));
        assertTrue(exception.getMessage().contains("NonExistentClass"));
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equals_hashCode() {
        ListSessionCommand command1 = new ListSessionCommand("Math101");
        ListSessionCommand command2 = new ListSessionCommand("Math101");
        ListSessionCommand command3 = new ListSessionCommand("Science101");

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        assertTrue(command1.equals(command2));

        // different values -> returns false
        assertFalse(command1.equals(command3));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different types -> returns false
        assertFalse(command1.equals("Not a command"));

        // equal objects have equal hash codes
        assertEquals(command1.hashCode(), command2.hashCode());
    }
}
