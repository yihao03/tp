package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.classes.UniqueClassList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueClassList classes;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication between
     * constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication among
     * constructors.
     */
    {
        persons = new UniquePersonList();
        classes = new UniqueClassList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// class-level operations

    /**
     * Returns true if a class with the same identity as {@code classToCheck} exists in the address book.
     */
    public boolean hasClass(seedu.address.model.classes.Class classToCheck) {
        requireNonNull(classToCheck);
        return classes.contains(classToCheck);
    }

    /**
     * Adds a class to the address book.
     * The class must not already exist in the address book.
     */
    public void addClass(seedu.address.model.classes.Class c) {
        classes.add(c);
    }

    /**
     * Replaces the given class {@code target} in the list with {@code editedClass}.
     * {@code target} must exist in the address book.
     * The class identity of {@code editedClass} must not be the same as another existing class in the address book.
     */
    public void setClass(seedu.address.model.classes.Class target, seedu.address.model.classes.Class editedClass) {
        requireNonNull(editedClass);

        classes.setClass(target, editedClass);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeClass(seedu.address.model.classes.Class key) {
        classes.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("persons", persons).add("classes", classes).toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    /**
     * Returns an unmodifiable view of the classes list.
     */
    public ObservableList<seedu.address.model.classes.Class> getClassList() {
        return classes.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons) && classes.equals(otherAddressBook.classes);
    }

    @Override
    public int hashCode() {
        return persons.hashCode() + classes.hashCode();
    }
}
