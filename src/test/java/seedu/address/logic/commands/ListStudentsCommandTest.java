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
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;

public class ListStudentsCommandTest {

    @Test
    public void constructor_nullClassName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListStudentsCommand(null));
    }

    @Test
    public void execute_classNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        ListStudentsCommand command = new ListStudentsCommand("NonExistentClass");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_classWithNoStudents_success() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        model.addClass(tuitionClass);

        ListStudentsCommand command = new ListStudentsCommand("Math101");
        CommandResult result = command.execute(model);

        assertEquals("[No students in this class]", result.getFeedbackToUser());
        assertEquals(CommandResult.DisplayType.NONE, result.getDisplayType());
    }

    @Test
    public void execute_classWithStudents_success() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        Student student1 = new Student(
                new Name("Alice Tan"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("123 Main St"),
                new java.util.HashSet<>()
        );
        Student student2 = new Student(
                new Name("Bob Lee"),
                new Phone("98765432"),
                new Email("bob@example.com"),
                new Address("456 Second St"),
                new java.util.HashSet<>()
        );

        model.addPerson(student1);
        model.addPerson(student2);

        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        model.addClass(tuitionClass);
        model.addStudentToClass(student1, tuitionClass);
        model.addStudentToClass(student2, tuitionClass);

        ListStudentsCommand command = new ListStudentsCommand("Math101");
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("Displaying 2 student(s)"));
        assertTrue(result.getFeedbackToUser().contains("Math101"));
        assertEquals(CommandResult.DisplayType.NONE, result.getDisplayType());
    }

    @Test
    public void execute_caseInsensitiveClassName_success() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        model.addClass(tuitionClass);

        ListStudentsCommand command = new ListStudentsCommand("math101");
        CommandResult result = command.execute(model);

        assertEquals("[No students in this class]", result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        ListStudentsCommand command1 = new ListStudentsCommand("Math101");
        ListStudentsCommand command2 = new ListStudentsCommand("Math101");
        ListStudentsCommand command3 = new ListStudentsCommand("Science101");

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        assertTrue(command1.equals(command2));

        // different values -> returns false
        assertFalse(command1.equals(command3));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different types -> returns false
        assertFalse(command1.equals(5));
    }

    @Test
    public void toStringMethod() {
        ListStudentsCommand command = new ListStudentsCommand("Math101");
        String expected = "ListStudentsCommand[className=Math101]";
        assertEquals(expected, command.toString());
    }
}
