package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.classroom.ClassSession;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<TuitionClass> PREDICATE_SHOW_ALL_CLASSES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in
     * the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns true if a tuitionClass with the same identity as {@code tuitionClass}
     * exists in the address book.
     */
    boolean hasClass(TuitionClass tuitionClass);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Adds the given tuition class.
     * {@code tuitionClass} must not already exist in the address book.
     */
    void addClass(TuitionClass tuitionClass);

    /**
     * Deletes the given tuition class.
     * The tuition class must exist in the address book.
     */
    void deleteClass(TuitionClass target);

    /**
     * Replaces the given tuition class {@code target} with {@code editedClass}.
     * {@code target} must exist in the address book.
     * The class identity of {@code editedClass} must not be the same as another
     * existing class in the address book.
     */
    void setClass(TuitionClass target, TuitionClass editedClass);

    /**
     * Adds the given person to the specified class.
     * {@code student} must already exist in the address book.
     * {@code toJoin} must already exist in the address book.
     */
    void addStudentToClass(Student student, TuitionClass toJoin);

    /**
     * Adsigns the given tutor to the specified class.
     * {@code tutor} must already exist in the address book.
     * {@code toJoin} must already exist in the address book.
     */
    void assignTutorToClass(Tutor tutor, TuitionClass toJoin);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another
     * existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the person list, unfiltered */
    ObservableList<Person> getPersonList();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered class list */
    ObservableList<TuitionClass> getFilteredClassList();

    /** Returns the class by its name */
    TuitionClass getClassByName(String className);

    /**
     * Updates the filter of the filtered person list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered class list to filter by the given
     * {@code class}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredClassList(Predicate<TuitionClass> predicate);

    /** Returns an unmodifiable view of the filtered session list */
    ObservableList<ClassSession> getFilteredSessionList();

    /**
     * Updates the filter of the filtered session list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredSessionList(Predicate<ClassSession> predicate);

    /**
     * Replaces the target session with editedSession in the session list.
     * Used to update a single session in the observable list (e.g., when attendance changes).
     *
     * @param target the session to replace
     * @param editedSession the session with updated data
     */
    void setSession(ClassSession target, ClassSession editedSession);

    /**
     * Sets the session list to display in the UI.
     *
     * @param sessions the list of sessions to display
     */
    void setSessionList(List<ClassSession> sessions);

    /**
     * Updates the UI session list for the given class, sorted by datetime in descending order.
     *
     * @param tuitionClass the class whose sessions should be displayed
     */
    void updateSessionListForClass(TuitionClass tuitionClass);

    /**
     * Clears the session list display in the UI
     */
    void clearSessions();
}
