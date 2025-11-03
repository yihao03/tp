package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
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
import seedu.address.model.person.Tutor;

public class ListParentsCommandTest {

    private Model model;
    private Parent parent1;
    private Parent parent2;
    private Parent parent3;
    private Student child1;
    private Student child2;
    private Tutor tutor;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        parent1 = new Parent(
                new Name("John Doe"),
                new Phone("91234567"),
                new Email("john@example.com"),
                new Address("123 Main St"),
                new HashSet<>()
        );

        parent2 = new Parent(
                new Name("Jane Smith"),
                new Phone("92345678"),
                new Email("jane@example.com"),
                new Address("456 Oak St"),
                new HashSet<>()
        );

        parent3 = new Parent(
                new Name("Robert Johnson"),
                new Phone("93456789"),
                new Email("robert@example.com"),
                new Address("789 Pine St"),
                new HashSet<>()
        );

        child1 = new Student(
                new Name("Alice Tan"),
                new Phone("94567890"),
                new Email("alice@example.com"),
                new Address("123 Main St"),
                new HashSet<>()
        );

        child2 = new Student(
                new Name("Bob Lee"),
                new Phone("95678901"),
                new Email("bob@example.com"),
                new Address("456 Oak St"),
                new HashSet<>()
        );

        tutor = new Tutor(
                new Name("Teacher Tom"),
                new Phone("97890123"),
                new Email("tom@example.com"),
                new Address("321 School St"),
                new HashSet<>()
        );
    }

    // ========== Success Cases ==========

    @Test
    public void execute_singleParent_filtersCorrectly() throws Exception {
        model.addPerson(child1);
        model.addPerson(parent1);
        model.addPerson(parent2); // Extra parent not linked to child
        child1.addParent(parent1);

        ListParentsCommand command = new ListParentsCommand("Alice Tan");
        CommandResult result = command.execute(model);

        // Check filtered list contains only parent1
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(parent1));
        assertFalse(model.getFilteredPersonList().contains(parent2));
        assertTrue(result.getFeedbackToUser().contains("1 shown"));
    }

    @Test
    public void execute_multipleParents_filtersCorrectly() throws Exception {
        model.addPerson(child1);
        model.addPerson(parent1);
        model.addPerson(parent2);
        model.addPerson(parent3);
        child1.addParent(parent1);
        child1.addParent(parent2);

        ListParentsCommand command = new ListParentsCommand("Alice Tan");
        CommandResult result = command.execute(model);

        // Check filtered list contains both parents
        assertEquals(2, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(parent1));
        assertTrue(model.getFilteredPersonList().contains(parent2));
        assertFalse(model.getFilteredPersonList().contains(parent3));
        assertTrue(result.getFeedbackToUser().contains("2 shown"));
    }

    @Test
    public void execute_noParents_showsEmptyList() throws Exception {
        model.addPerson(child1);
        model.addPerson(parent1);

        ListParentsCommand command = new ListParentsCommand("Alice Tan");
        CommandResult result = command.execute(model);

        assertEquals(0, model.getFilteredPersonList().size());
        assertTrue(result.getFeedbackToUser().contains("[No parents]"));
    }

    @Test
    public void execute_caseInsensitive_success() throws Exception {
        model.addPerson(child1);
        model.addPerson(parent1);
        child1.addParent(parent1);

        ListParentsCommand command = new ListParentsCommand("ALICE TAN");
        CommandResult result = command.execute(model);

        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(parent1));
    }

    @Test
    public void execute_multipleSiblingsDifferentParents_filtersCorrectly() throws Exception {
        model.addPerson(child1);
        model.addPerson(child2);
        model.addPerson(parent1);
        model.addPerson(parent2);

        child1.addParent(parent1);
        child2.addParent(parent2);

        // Test child1
        ListParentsCommand command1 = new ListParentsCommand("Alice Tan");
        command1.execute(model);
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(parent1));

        // Test child2
        ListParentsCommand command2 = new ListParentsCommand("Bob Lee");
        command2.execute(model);
        assertEquals(1, model.getFilteredPersonList().size());
        assertTrue(model.getFilteredPersonList().contains(parent2));
    }

    // ========== Failure Cases ==========

    @Test
    public void execute_childNotFound_throwsCommandException() {
        model.addPerson(parent1);
        ListParentsCommand command = new ListParentsCommand("Non-existent Child");
        assertThrows(CommandException.class, "Child not found: Non-existent Child", () ->
                command.execute(model));
    }

    @Test
    public void execute_tutorNameProvided_throwsCommandException() {
        model.addPerson(tutor);
        ListParentsCommand command = new ListParentsCommand("Teacher Tom");
        assertThrows(CommandException.class, "Child not found: Teacher Tom", () ->
                command.execute(model));
    }

    @Test
    public void execute_parentNameProvided_throwsCommandException() {
        model.addPerson(parent1);
        ListParentsCommand command = new ListParentsCommand("John Doe");
        assertThrows(CommandException.class, "Child not found: John Doe", () ->
                command.execute(model));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ListParentsCommand command = new ListParentsCommand("Alice Tan");
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    // ========== Equals Tests ==========

    @Test
    public void equals_sameObject_returnsTrue() {
        ListParentsCommand command = new ListParentsCommand("Alice");
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_sameChildName_returnsTrue() {
        ListParentsCommand command1 = new ListParentsCommand("Alice");
        ListParentsCommand command2 = new ListParentsCommand("Alice");
        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_differentChildName_returnsFalse() {
        ListParentsCommand command1 = new ListParentsCommand("Alice");
        ListParentsCommand command2 = new ListParentsCommand("Bob");
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_null_returnsFalse() {
        ListParentsCommand command = new ListParentsCommand("Alice");
        assertFalse(command.equals(null));
    }
}
