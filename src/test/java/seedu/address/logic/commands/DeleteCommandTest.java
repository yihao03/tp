package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_deleteParent_removesParentFromChildren() {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create parent and student
        Person parent = new PersonBuilder().withName("Parent").withPhone("91234567")
                .withPersonType(PersonType.PARENT).build();
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(parent);
        modelWithRelationships.addPerson(student);

        // Establish relationship
        Parent parentCast = (Parent) parent;
        Student studentCast = (Student) student;
        parentCast.addChild(studentCast);

        // Verify relationship exists
        assertTrue(studentCast.getParents().contains(parent));

        // Delete parent
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1));
        try {
            deleteCommand.execute(modelWithRelationships);
        } catch (Exception e) {
            throw new AssertionError("Command should not fail", e);
        }

        // Verify parent removed from student
        assertEquals(0, studentCast.getParents().size());
    }

    @Test
    public void execute_deleteStudent_removesStudentFromParentsAndClasses() {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create parent, student, and tuition class
        Person parent = new PersonBuilder().withName("Parent").withPhone("91234567")
                .withPersonType(PersonType.PARENT).build();
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(parent);
        modelWithRelationships.addPerson(student);

        // Establish relationships
        Parent parentCast = (Parent) parent;
        Student studentCast = (Student) student;
        parentCast.addChild(studentCast);

        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        tuitionClass.addStudent(studentCast);

        // Verify relationships exist
        assertTrue(parentCast.getChildren().contains(student));
        assertTrue(tuitionClass.getStudents().contains(student));

        // Delete student
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(2));
        try {
            deleteCommand.execute(modelWithRelationships);
        } catch (Exception e) {
            throw new AssertionError("Command should not fail", e);
        }

        // Verify student removed from parent and class
        assertEquals(0, parentCast.getChildren().size());
        assertEquals(0, tuitionClass.getStudents().size());
    }

    @Test
    public void execute_deleteTutor_removesTutorFromClass() {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create tutor and tuition class
        Person tutor = new PersonBuilder().withName("Tutor").withPhone("91234567")
                .withPersonType(PersonType.TUTOR).build();

        modelWithRelationships.addPerson(tutor);

        // Establish relationship
        Tutor tutorCast = (Tutor) tutor;
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        tuitionClass.setTutor(tutorCast);

        // Verify relationship exists
        assertTrue(tuitionClass.isAssignedToTutor());
        assertEquals(tutor, tuitionClass.getTutor());

        // Delete tutor
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(1));
        try {
            deleteCommand.execute(modelWithRelationships);
        } catch (Exception e) {
            throw new AssertionError("Command should not fail", e);
        }

        // Verify tutor removed from class (tutor becomes null)
        assertFalse(tuitionClass.isAssignedToTutor());
    }
}
