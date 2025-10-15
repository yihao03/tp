package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void hasClass_nullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasClass(null));
    }

    @Test
    public void hasClass_classNotInAddressBook_returnsFalse() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        assertFalse(modelManager.hasClass(cls));
    }

    @Test
    public void hasClass_classInAddressBook_returnsTrue() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        modelManager.addClass(cls);
        assertTrue(modelManager.hasClass(cls));
    }

    @Test
    public void addClass_validClass_addsSuccessfully() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        modelManager.addClass(cls);
        assertTrue(modelManager.hasClass(cls));
    }

    @Test
    public void deleteClass_existingClass_removesClass() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        modelManager.addClass(cls);
        modelManager.deleteClass(cls);
        assertFalse(modelManager.hasClass(cls));
    }

    @Test
    public void setClass_validClass_setsSuccessfully() {
        seedu.address.model.classes.Class original = new seedu.address.model.classes.Class("CS2103T", "SE");
        seedu.address.model.classes.Class edited = new seedu.address.model.classes.Class("CS2040C", "Algo");
        modelManager.addClass(original);
        modelManager.setClass(original, edited);
        assertFalse(modelManager.hasClass(original));
        assertTrue(modelManager.hasClass(edited));
    }

    @Test
    public void setClass_nullTarget_throwsNullPointerException() {
        seedu.address.model.classes.Class edited = new seedu.address.model.classes.Class("CS2040C", "Algo");
        assertThrows(NullPointerException.class, () -> modelManager.setClass(null, edited));
    }

    @Test
    public void setClass_nullEdited_throwsNullPointerException() {
        seedu.address.model.classes.Class original = new seedu.address.model.classes.Class("CS2103T", "SE");
        modelManager.addClass(original);
        assertThrows(NullPointerException.class, () -> modelManager.setClass(original, null));
    }

    @Test
    public void getFilteredClassList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredClassList().remove(0));
    }

    @Test
    public void updateFilteredClassList_validPredicate_filtersClasses() {
        seedu.address.model.classes.Class cls1 = new seedu.address.model.classes.Class("CS2103T", "SE");
        seedu.address.model.classes.Class cls2 = new seedu.address.model.classes.Class("CS2040C", "Algo");
        modelManager.addClass(cls1);
        modelManager.addClass(cls2);

        modelManager.updateFilteredClassList(cls -> cls.getClassName().contains("CS2103T"));
        assertEquals(1, modelManager.getFilteredClassList().size());
        assertTrue(modelManager.getFilteredClassList().contains(cls1));
    }

    @Test
    public void updateFilteredClassList_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredClassList(null));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
