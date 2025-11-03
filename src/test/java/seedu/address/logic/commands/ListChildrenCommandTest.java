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
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;

public class ListChildrenCommandTest {

    @Test
    public void execute_multipleChildren_filtersCorrectly() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        Student student1 = new Student(
                new Name("Charlie Wong"),
                new Phone("91234567"),
                new Email("charlie@example.com"),
                new Address("123 Main St"),
                new java.util.HashSet<>()
        );
        Student student2 = new Student(
                new Name("David Lim"),
                new Phone("98765432"),
                new Email("david@example.com"),
                new Address("456 Second St"),
                new java.util.HashSet<>()
        );
        Student student3 = new Student(
                new Name("Emily Tan"),
                new Phone("99999999"),
                new Email("emily@example.com"),
                new Address("999 Road"),
                new java.util.HashSet<>()
        );
        Parent parent = new Parent(
                new Name("John Doe"),
                new Phone("87654321"),
                new Email("john@example.com"),
                new Address("789 Third St"),
                new java.util.HashSet<>()
        );

        model.addPerson(student1);
        model.addPerson(student2);
        model.addPerson(student3);
        model.addPerson(parent);

        parent.addChild(student1);
        parent.addChild(student2);

        ListChildrenCommand command = new ListChildrenCommand("John Doe");
        CommandResult result = command.execute(model);

        // Check filtered list contains only the children
        assertEquals(2, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(student1));
        assertTrue(model.getFilteredPersonList().contains(student2));
        assertFalse(model.getFilteredPersonList().contains(student3));
        assertTrue(result.getFeedbackToUser().contains("2 shown"));
    }

    @Test
    public void execute_noChildren_showsEmptyList() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        Student student = new Student(
                new Name("Emily Tan"),
                new Phone("91234567"),
                new Email("emily@example.com"),
                new Address("123 Main St"),
                new java.util.HashSet<>()
        );
        Parent parent = new Parent(
                new Name("Jane Smith"),
                new Phone("87654321"),
                new Email("jane@example.com"),
                new Address("789 Third St"),
                new java.util.HashSet<>()
        );

        model.addPerson(student);
        model.addPerson(parent);

        ListChildrenCommand command = new ListChildrenCommand("Jane Smith");
        CommandResult result = command.execute(model);

        assertEquals(0, model.getFilteredPersonList().size());
        assertTrue(result.getFeedbackToUser().contains("[No children]"));
    }

    @Test
    public void execute_caseInsensitive_success() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        Student student = new Student(
                new Name("Emily Tan"),
                new Phone("91234567"),
                new Email("emily@example.com"),
                new Address("123 Main St"),
                new java.util.HashSet<>()
        );
        Parent parent = new Parent(
                new Name("John Doe"),
                new Phone("87654321"),
                new Email("john@example.com"),
                new Address("789 Third St"),
                new java.util.HashSet<>()
        );

        model.addPerson(student);
        model.addPerson(parent);
        parent.addChild(student);

        ListChildrenCommand command = new ListChildrenCommand("john doe");
        CommandResult result = command.execute(model);

        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(student));
    }

    @Test
    public void execute_parentNotFound_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        ListChildrenCommand command = new ListChildrenCommand("NonExistent Parent");
        assertThrows(CommandException.class, "Parent not found: NonExistent Parent", () ->
                command.execute(model));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ListChildrenCommand command = new ListChildrenCommand("John Doe");
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ListChildrenCommand command = new ListChildrenCommand("John Doe");
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_sameParentName_returnsTrue() {
        ListChildrenCommand command1 = new ListChildrenCommand("John Doe");
        ListChildrenCommand command2 = new ListChildrenCommand("John Doe");
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentParentName_returnsFalse() {
        ListChildrenCommand command1 = new ListChildrenCommand("John Doe");
        ListChildrenCommand command2 = new ListChildrenCommand("Jane Smith");
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_null_returnsFalse() {
        ListChildrenCommand command = new ListChildrenCommand("John Doe");
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        ListChildrenCommand command = new ListChildrenCommand("John Doe");
        assertFalse(command.equals(1));
    }
}
