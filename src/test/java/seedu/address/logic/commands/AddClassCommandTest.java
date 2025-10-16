package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;

public class AddClassCommandTest {

    @Test
    public void constructor_nullClassName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddClassCommand(null));
    }

    @Test
    public void execute_classAcceptedByModel_addSuccessful() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        TuitionClass validClass = new TuitionClass(new ClassName("Sec1-Math-A"));

        CommandResult commandResult = new AddClassCommand("Sec1-Math-A").execute(model);

        assertEquals(String.format(AddClassCommand.MESSAGE_SUCCESS, validClass.getName().value),
                commandResult.getFeedbackToUser());
        assertTrue(model.hasClass(validClass));
    }

    @Test
    public void execute_duplicateClass_throwsCommandException() {
        TuitionClass validClass = new TuitionClass(new ClassName("Sec1-Math-A"));
        AddClassCommand addClassCommand = new AddClassCommand("Sec1-Math-A");
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addClass(validClass);

        assertThrows(CommandException.class, AddClassCommand.MESSAGE_DUPLICATE_CLASS, (
                ) -> addClassCommand.execute(model));
    }

    @Test
    public void execute_classWithTutor_addSuccessful() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        seedu.address.model.person.Tutor tutor = new seedu.address.model.person.Tutor(
                new seedu.address.model.person.Name("Ms Lee"),
                new seedu.address.model.person.Phone("98765432"),
                new seedu.address.model.person.Email("lee@example.com"),
                new seedu.address.model.person.Address("456 School St"),
                new java.util.HashSet<>()
        );
        model.addPerson(tutor);

        CommandResult commandResult = new AddClassCommand("Sec1-Math-A", "Ms Lee").execute(model);

        assertTrue(commandResult.getFeedbackToUser().contains("New class added"));
        assertTrue(model.hasClass(new TuitionClass(new ClassName("Sec1-Math-A"))));
    }

    @Test
    public void execute_tutorNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        AddClassCommand command = new AddClassCommand("Sec1-Math-A", "NonExistent Tutor");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_ambiguousTutor_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        seedu.address.model.person.Tutor tutor1 = new seedu.address.model.person.Tutor(
                new seedu.address.model.person.Name("Jane Doe"),
                new seedu.address.model.person.Phone("98765432"),
                new seedu.address.model.person.Email("jane@example.com"),
                new seedu.address.model.person.Address("456 School St"),
                new java.util.HashSet<>()
        );
        seedu.address.model.person.Tutor tutor2 = new seedu.address.model.person.Tutor(
                new seedu.address.model.person.Name("jane doe"),
                new seedu.address.model.person.Phone("91234567"),
                new seedu.address.model.person.Email("janedoe@example.com"),
                new seedu.address.model.person.Address("789 College Rd"),
                new java.util.HashSet<>()
        );
        model.addPerson(tutor1);
        model.addPerson(tutor2);

        AddClassCommand command = new AddClassCommand("Sec1-Math-A", "Jane Doe");
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void equals() {
        AddClassCommand addClassCommand = new AddClassCommand("Sec1-Math-A");
        AddClassCommand addClassCommandCopy = new AddClassCommand("Sec1-Math-A");
        AddClassCommand addClassCommandDifferent = new AddClassCommand("Sec2-Math-B");
        AddClassCommand addClassCommandWithTutor = new AddClassCommand("Sec1-Math-A", "Ms Lee");
        AddClassCommand addClassCommandWithTutorCopy = new AddClassCommand("Sec1-Math-A", "Ms Lee");
        AddClassCommand addClassCommandWithDifferentTutor = new AddClassCommand("Sec1-Math-A", "Mr Smith");

        assertTrue(addClassCommand.equals(addClassCommand));

        assertTrue(addClassCommand.equals(addClassCommandCopy));

        assertFalse(addClassCommand.equals(1));

        assertFalse(addClassCommand.equals(null));

        assertFalse(addClassCommand.equals(addClassCommandDifferent));

        assertFalse(addClassCommand.equals(addClassCommandWithTutor));

        assertTrue(addClassCommandWithTutor.equals(addClassCommandWithTutorCopy));

        assertFalse(addClassCommandWithTutor.equals(addClassCommandWithDifferentTutor));
    }
}
