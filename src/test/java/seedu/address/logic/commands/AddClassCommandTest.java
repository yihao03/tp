package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.classes.Class;
import seedu.address.model.person.Person;

public class AddClassCommandTest {

    @Test
    public void constructor_nullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddClassCommand(null));
    }

    @Test
    public void execute_classAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingClassAdded modelStub = new ModelStubAcceptingClassAdded();
        Class validClass = new Class("CS2103T", "SE");

        CommandResult commandResult = new AddClassCommand(validClass).execute(modelStub);

        assertEquals(String.format(AddClassCommand.MESSAGE_SUCCESS, Messages.format(validClass)),
                commandResult.getFeedbackToUser());
        assertTrue(modelStub.classesAdded.stream().anyMatch(c -> c.equals(validClass)));
    }

    @Test
    public void execute_duplicateClass_throwsCommandException() {
        Class validClass = new Class("CS2103T", "SE");
        AddClassCommand addClassCommand = new AddClassCommand(validClass);
        ModelStub modelStub = new ModelStubWithClass(validClass);

        assertThrows(CommandException.class, AddClassCommand.MESSAGE_DUPLICATE_CLASS, () ->
            addClassCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Class a = new Class("CS2103T", "SE");
        Class b = new Class("CS2040C", "Algo");
        AddClassCommand addA = new AddClassCommand(a);
        AddClassCommand addB = new AddClassCommand(b);

        // same object -> true
        assertTrue(addA.equals(addA));

        // same values -> true
        AddClassCommand addACopy = new AddClassCommand(a);
        assertTrue(addA.equals(addACopy));

        // different types -> false
        assertFalse(addA.equals(1));

        // null -> false
        assertFalse(addA.equals(null));

        // different class -> false
        assertFalse(addA.equals(addB));
    }

    @Test
    public void toStringMethod() {
        Class a = new Class("CS2103T", "SE");
        AddClassCommand cmd = new AddClassCommand(a);
        String expected = AddClassCommand.class.getCanonicalName() + "{toAdd=" + a + "}";
        assertEquals(expected, cmd.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addClass(Class classe) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasClass(Class classe) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteClass(Class target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setClass(Class target, Class editedClass) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Class> getFilteredClassList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredClassList(Predicate<Class> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /** A Model stub that contains a single class. */
    private class ModelStubWithClass extends ModelStub {
        private final Class classe;

        ModelStubWithClass(Class classe) {
            requireNonNull(classe);
            this.classe = classe;
        }

        @Override
        public boolean hasClass(Class classe) {
            requireNonNull(classe);
            return this.classe.equals(classe);
        }
    }

    /** A Model stub that always accepts the class being added. */
    private class ModelStubAcceptingClassAdded extends ModelStub {
        final ArrayList<Class> classesAdded = new ArrayList<>();

        @Override
        public boolean hasClass(Class classe) {
            requireNonNull(classe);
            return classesAdded.stream().anyMatch(c -> c.equals(classe));
        }

        @Override
        public void addClass(Class classe) {
            requireNonNull(classe);
            classesAdded.add(classe);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
