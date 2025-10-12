package seedu.address.model.classes;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.ClassNotFoundException;
import seedu.address.model.person.exceptions.DuplicateClassException;

/**
 * A list of classes that enforces uniqueness between its elements and does not allow nulls.
 * A class is considered unique by comparing class names using case-insensitive comparison.
 * As such, adding and updating of classes uses class name equality to ensure that the class
 * being added or updated is unique in terms of identity in the UniqueClassList.
 *
 * Supports a minimal set of list operations.
 */
public class UniqueClassList implements Iterable<Class> {

    private final ObservableList<Class> internalList = FXCollections.observableArrayList();
    private final ObservableList<Class> internalUnmodifiableList = FXCollections
                    .unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent class as the given argument.
     * Classes are considered equivalent if they have the same class name (case-insensitive).
     */
    public boolean contains(Class toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(c -> isSameClass(c, toCheck));
    }

    /**
     * Returns true if both classes have the same class name (case-insensitive).
     */
    private boolean isSameClass(Class class1, Class class2) {
        return class1.getClassName().equalsIgnoreCase(class2.getClassName());
    }

    /**
     * Adds a class to the list.
     * The class must not already exist in the list.
     */
    public void add(Class toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateClassException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the class {@code target} in the list with {@code editedClass}.
     * {@code target} must exist in the list.
     * The class identity of {@code editedClass} must not be the same as another existing class in the list.
     */
    public void setClass(Class target, Class editedClass) {
        requireAllNonNull(target, editedClass);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ClassNotFoundException();
        }

        if (!isSameClass(target, editedClass) && contains(editedClass)) {
            throw new DuplicateClassException();
        }

        internalList.set(index, editedClass);
    }

    /**
     * Removes the equivalent class from the list.
     * The class must exist in the list.
     */
    public void remove(Class toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ClassNotFoundException();
        }
    }

    public void setClasses(UniqueClassList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code classes}.
     * {@code classes} must not contain duplicate classes.
     */
    public void setClasses(List<Class> classes) {
        requireAllNonNull(classes);
        if (!classesAreUnique(classes)) {
            throw new DuplicateClassException();
        }

        internalList.setAll(classes);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Class> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Class> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueClassList)) {
            return false;
        }

        UniqueClassList otherUniqueClassList = (UniqueClassList) other;
        return internalList.equals(otherUniqueClassList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code classes} contains only unique classes.
     */
    private boolean classesAreUnique(List<Class> classes) {
        for (int i = 0; i < classes.size() - 1; i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                if (isSameClass(classes.get(i), classes.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
