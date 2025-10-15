package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

/**
 * Contains integration tests (interaction with the Model) for {@code DeleteClassCommand}.
 */
public class DeleteClassCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_existingClass_deleteSuccessful() throws CommandException {
        TuitionClass classToDelete = new TuitionClass(new ClassName("Math101"));
        model.addClass(classToDelete);

        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("Math101");
        CommandResult result = deleteClassCommand.execute(model);

        String expectedMessage = String.format(DeleteClassCommand.MESSAGE_DELETE_CLASS_SUCCESS, "Math101");
        assertEquals(expectedMessage, result.getFeedbackToUser());

        assertFalse(model.hasClass(classToDelete));
    }

    @Test
    public void execute_nonExistentClass_throwsCommandException() {
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("NonExistentClass");

        assertThrows(CommandException.class, String.format(DeleteClassCommand.MESSAGE_CLASS_NOT_FOUND,
                "NonExistentClass"), () -> deleteClassCommand.execute(model));
    }

    @Test
    public void execute_multipleClassesDeleteSequentially_allSuccessful() throws CommandException {
        TuitionClass class1 = new TuitionClass(new ClassName("Math101"));
        TuitionClass class2 = new TuitionClass(new ClassName("Physics201"));
        TuitionClass class3 = new TuitionClass(new ClassName("Chemistry301"));
        model.addClass(class1);
        model.addClass(class2);
        model.addClass(class3);

        new DeleteClassCommand("Math101").execute(model);
        new DeleteClassCommand("Physics201").execute(model);
        new DeleteClassCommand("Chemistry301").execute(model);

        assertFalse(model.hasClass(class1));
        assertFalse(model.hasClass(class2));
        assertFalse(model.hasClass(class3));
    }

    @Test
    public void execute_deleteClassTwice_throwsCommandException() throws CommandException {
        model.addClass(new TuitionClass(new ClassName("Math101")));
        new DeleteClassCommand("Math101").execute(model);

        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("Math101");
        assertThrows(CommandException.class, String.format(DeleteClassCommand.MESSAGE_CLASS_NOT_FOUND,
                "Math101"), () -> deleteClassCommand.execute(model));
    }

    @Test
    public void execute_deleteClassWithSpacesInName_successful() throws CommandException {
        TuitionClass classWithSpaces = new TuitionClass(new ClassName("Advanced Math 101"));
        model.addClass(classWithSpaces);

        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("Advanced Math 101");
        deleteClassCommand.execute(model);

        assertFalse(model.hasClass(classWithSpaces));
    }

    @Test
    public void execute_deleteFromEmptyClassList_throwsCommandException() {
        DeleteClassCommand deleteClassCommand = new DeleteClassCommand("AnyClass");

        assertThrows(CommandException.class, String.format(DeleteClassCommand.MESSAGE_CLASS_NOT_FOUND,
                "AnyClass"), () -> deleteClassCommand.execute(model));
    }
}
