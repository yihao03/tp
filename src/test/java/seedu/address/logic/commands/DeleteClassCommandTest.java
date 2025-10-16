package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;

public class DeleteClassCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_classNotFound_throwsCommandException() {
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("NonExistentClass");
        assertThrows(CommandException.class, () -> deleteClassCommand.execute(model));
    }

    @Test
    public void execute_validClass_deleteSuccessful() throws CommandException {
        TuitionClass classToDelete = new TuitionClass(new ClassName("ClassToDelete"));
        model.addClass(classToDelete);

        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("ClassToDelete");
        CommandResult result = deleteClassCommand.execute(model);

        String expectedMessage = String.format(DeleteClassCommand.MESSAGE_DELETE_CLASS_SUCCESS, "ClassToDelete");
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertFalse(model.hasClass(classToDelete));
    }

    @Test
    public void hashCode_sameClassName_equalHashCode() {
        DeleteClassCommand first = new DeleteClassCommand("Class1");
        DeleteClassCommand second = new DeleteClassCommand("Class1");
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void equals() {
        DeleteClassCommand deleteClass1 = new DeleteClassCommand("Class1");
        DeleteClassCommand deleteClass2 = new DeleteClassCommand("Class2");
        DeleteClassCommand deleteClass1Copy = new DeleteClassCommand("Class1");

        assertTrue(deleteClass1.equals(deleteClass1));
        assertTrue(deleteClass1.equals(deleteClass1Copy));
        assertFalse(deleteClass1.equals(deleteClass2));
        assertFalse(deleteClass1.equals(null));
        assertFalse(deleteClass1.equals(new ClearCommand()));
    }
}
