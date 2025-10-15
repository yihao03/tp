package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                        .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                        .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void hasClass_nullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasClass(null));
    }

    @Test
    public void hasClass_classNotInAddressBook_returnsFalse() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        assertFalse(addressBook.hasClass(cls));
    }

    @Test
    public void hasClass_classInAddressBook_returnsTrue() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        addressBook.addClass(cls);
        assertTrue(addressBook.hasClass(cls));
    }

    @Test
    public void addClass_validClass_success() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        addressBook.addClass(cls);
        assertTrue(addressBook.hasClass(cls));
    }

    @Test
    public void removeClass_existingClass_removesClass() {
        seedu.address.model.classes.Class cls = new seedu.address.model.classes.Class("CS2103T", "SE");
        addressBook.addClass(cls);
        addressBook.removeClass(cls);
        assertFalse(addressBook.hasClass(cls));
    }

    @Test
    public void setClass_validClass_success() {
        seedu.address.model.classes.Class original = new seedu.address.model.classes.Class("CS2103T", "SE");
        seedu.address.model.classes.Class edited = new seedu.address.model.classes.Class("CS2040C", "Algo");
        addressBook.addClass(original);
        addressBook.setClass(original, edited);
        assertFalse(addressBook.hasClass(original));
        assertTrue(addressBook.hasClass(edited));
    }

    @Test
    public void setClass_nullClass_throwsNullPointerException() {
        seedu.address.model.classes.Class original = new seedu.address.model.classes.Class("CS2103T", "SE");
        addressBook.addClass(original);
        assertThrows(NullPointerException.class, () -> addressBook.setClass(original, null));
    }

    @Test
    public void getClassList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getClassList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList()
                        + ", classes=" + addressBook.getClassList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<seedu.address.model.classes.Class> classes = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        public ObservableList<seedu.address.model.classes.Class> getClassList() {
            return classes;
        }
    }
}
