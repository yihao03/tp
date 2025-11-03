package seedu.address.model.person;

import java.util.ArrayList;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents a Tutor in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Tutor extends Person {
    /**
     * Tuition classes this tutor teaches.
     * Using ObservableList allows UI to bind directly and be notified of changes.
     */
    private ObservableList<TuitionClass> tuitionClasses = FXCollections.observableArrayList();

    /**
     * Constructs a {@code Tutor}.
     *
     * @param name A valid name.
     * @param phone A valid phone number.
     * @param email A valid email address.
     * @param address A valid address.
     * @param tags A set of tags.
     */
    public Tutor(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    /**
     * Returns an unmodifiable view of the tuition classes list.
     * This allows UI components to observe changes while preventing external modifications.
     */
    @Override
    public ObservableList<TuitionClass> getTuitionClasses() {
        return FXCollections.unmodifiableObservableList(tuitionClasses);
    }

    /**
     * Adds this tutor to a tuition class if not already teaching it.
     */
    public void addClass(TuitionClass tuitionClass) {
        if (!tuitionClasses.contains(tuitionClass)) {
            tuitionClasses.add(tuitionClass);
        }
    }

    /**
     * Removes this tutor from a tuition class.
     * @throws PersonNotFoundException if not teaching this class
     */
    public void unjoin(TuitionClass tuitionClass) {
        if (!tuitionClasses.remove(tuitionClass)) {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Safely removes a tuition class from this tutor's list without throwing exception.
     * @return true if the class was removed, false if not teaching
     */
    public boolean unjoinSafely(TuitionClass tuitionClass) {
        return tuitionClasses.remove(tuitionClass);
    }

    /**
     * Removes this tutor from all tuition classes.
     */
    public void unjoinAllClasses() {
        tuitionClasses.forEach(tuitionClass -> tuitionClass.removeTutor(this));
    }

    /**
     * Changes all tuition classes' tutor to point to the newly editedTutor instead.
     * The bidirectional relationship is automatically maintained by setTutor.
     */
    public void editTuitionClassMappings(Tutor editedTutor) {
        // Create a copy to avoid ConcurrentModificationException
        // (setTutor modifies the list we're iterating over)
        new ArrayList<>(tuitionClasses).forEach(tuitionClass -> tuitionClass.setTutor(editedTutor));
    }

    /**
     * Returns the type of this person.
     *
     * @return The person type TUTOR.
     */
    @Override
    public PersonType getPersonType() {
        return PersonType.TUTOR;
    }

    /**
     * Removes tutor entirely
     */
    @Override
    public void delete() {
        // Remove tutor from associated classes
        this.unjoinAllClasses();
    }

    /**
     * Handles editing of this tutor by updating or removing relationships.
     */
    @Override
    public void handleEdit(Person editedPerson, boolean isTypeEdited) {
        if (isTypeEdited) {
            // Type changed, remove this tutor from all classes
            this.unjoinAllClasses();
        } else {
            // Type unchanged, update bidirectional relationships
            Tutor editedTutor = (Tutor) editedPerson;
            this.editTuitionClassMappings(editedTutor);
        }
    }
}
