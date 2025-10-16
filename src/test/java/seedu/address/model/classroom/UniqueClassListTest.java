package seedu.address.model.classroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.classroom.exceptions.ClassNotFoundException;
import seedu.address.model.classroom.exceptions.DuplicateClassException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Tutor;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for {@link UniqueClassList}.
 */
public class UniqueClassListTest {

    private UniqueClassList classList;
    private Tutor tutor1;
    private Tutor tutor2;
    private TuitionClass class1;
    private TuitionClass class2;
    private TuitionClass class3;

    @BeforeEach
    void setUp() {
        classList = new UniqueClassList();
        Set<Tag> emptyTags = new HashSet<>();

        tutor1 = new Tutor(
                new Name("Mr Smith"),
                new Phone("91234567"),
                new Email("smith@example.com"),
                new Address("1 Tutor Lane"),
                emptyTags
        );

        tutor2 = new Tutor(
                new Name("Ms Lee"),
                new Phone("91239876"),
                new Email("lee@example.com"),
                new Address("5 Learning Street"),
                emptyTags
        );

        class1 = new TuitionClass(new ClassName("CS2103T T12"), tutor1);
        class2 = new TuitionClass(new ClassName("CS2103T T13"), tutor2);
        class3 = new TuitionClass(new ClassName("CS2103T T14"), tutor1);
    }

    @Test
    @DisplayName("Add class successfully")
    void add_success() {
        classList.add(class1);
        assertTrue(classList.contains(class1));
        assertEquals(1, classList.asUnmodifiableObservableList().size());
    }

    @Test
    @DisplayName("Adding duplicate class throws DuplicateClassException")
    void add_duplicate_throws() {
        classList.add(class1);
        assertThrows(DuplicateClassException.class, () -> classList.add(class1));
    }

    @Test
    @DisplayName("Remove class successfully")
    void remove_success() {
        classList.add(class1);
        classList.add(class2);

        classList.remove(class1);
        assertFalse(classList.contains(class1));
        assertEquals(1, classList.asUnmodifiableObservableList().size());
    }

    @Test
    @DisplayName("Removing non-existent class throws ClassNotFoundException")
    void remove_nonExistent_throws() {
        assertThrows(ClassNotFoundException.class, () -> classList.remove(class1));
    }

    @Test
    @DisplayName("Contains check works correctly")
    void contains_check() {
        classList.add(class1);
        assertTrue(classList.contains(class1));
        assertFalse(classList.contains(class2));
    }

    @Test
    @DisplayName("asUnmodifiableObservableList returns all classes and is unmodifiable")
    void asUnmodifiableObservableList_isUnmodifiable() {
        classList.add(class1);
        classList.add(class2);

        ObservableList<TuitionClass> list = classList.asUnmodifiableObservableList();
        assertEquals(2, list.size());
        assertTrue(list.contains(class1));
        assertTrue(list.contains(class2));

        // Ensure unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> list.add(class1));
        assertThrows(UnsupportedOperationException.class, () -> list.remove(class1));
    }

    @Test
    @DisplayName("Iterator iterates over all classes in insertion order")
    void iterator_works() {
        classList.add(class1);
        classList.add(class2);

        Iterator<TuitionClass> it = classList.asUnmodifiableObservableList().iterator();
        assertTrue(it.hasNext());
        assertEquals(class1, it.next());
        assertTrue(it.hasNext());
        assertEquals(class2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    @DisplayName("Size reflects number of classes")
    void size_reflectedByListSize() {
        assertEquals(0, classList.asUnmodifiableObservableList().size());
        classList.add(class1);
        assertEquals(1, classList.asUnmodifiableObservableList().size());
        classList.add(class2);
        assertEquals(2, classList.asUnmodifiableObservableList().size());
    }

    @Test
    @DisplayName("setClasses replaces classes successfully")
    void setClasses_validList_success() {
        classList.add(class1);

        java.util.List<TuitionClass> newClasses = java.util.Arrays.asList(class2, class3);
        classList.setClasses(newClasses);

        assertFalse(classList.contains(class1));
        assertTrue(classList.contains(class2));
        assertTrue(classList.contains(class3));
        assertEquals(2, classList.size());
    }

    @Test
    @DisplayName("setClasses handles multiple unique classes")
    void setClasses_multipleUniqueClasses_success() {
        java.util.List<TuitionClass> newClasses = java.util.Arrays.asList(class1, class2, class3);
        classList.setClasses(newClasses);

        assertEquals(3, classList.size());
        assertTrue(classList.contains(class1));
        assertTrue(classList.contains(class2));
        assertTrue(classList.contains(class3));
    }

    @Test
    @DisplayName("setClasses with duplicates throws DuplicateClassException")
    void setClasses_duplicateClasses_throws() {
        java.util.List<TuitionClass> duplicateClasses = java.util.Arrays.asList(class1, class3,
                new TuitionClass(new ClassName("CS2103T T14"), tutor1));
        assertThrows(DuplicateClassException.class, () -> classList.setClasses(duplicateClasses));
    }

    @Test
    @DisplayName("clear removes all classes")
    void clear_removesAllClasses() {
        classList.add(class1);
        classList.add(class2);
        classList.clear();
        assertEquals(0, classList.size());
        assertFalse(classList.contains(class1));
        assertFalse(classList.contains(class2));
    }

    @Test
    @DisplayName("size returns correct count")
    void size_returnsCorrectCount() {
        assertEquals(0, classList.size());
        classList.add(class1);
        assertEquals(1, classList.size());
        classList.add(class2);
        assertEquals(2, classList.size());
        classList.remove(class1);
        assertEquals(1, classList.size());
    }

    @Test
    @DisplayName("iterator method works correctly")
    void iterator_iteratesOverClasses() {
        classList.add(class1);
        classList.add(class2);

        java.util.Iterator<TuitionClass> iterator = classList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(class1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(class2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    @DisplayName("equals returns true for same object")
    void equals_sameObject_returnsTrue() {
        assertTrue(classList.equals(classList));
    }

    @Test
    @DisplayName("equals returns false for null")
    void equals_null_returnsFalse() {
        assertFalse(classList.equals(null));
    }

    @Test
    @DisplayName("equals returns false for different type")
    void equals_differentType_returnsFalse() {
        assertFalse(classList.equals("not a list"));
    }

    @Test
    @DisplayName("equals returns true for same contents")
    void equals_sameContents_returnsTrue() {
        UniqueClassList otherList = new UniqueClassList();
        classList.add(class1);
        otherList.add(class1);

        assertTrue(classList.equals(otherList));
    }

    @Test
    @DisplayName("equals returns false for different contents")
    void equals_differentContents_returnsFalse() {
        UniqueClassList otherList = new UniqueClassList();
        classList.add(class1);
        otherList.add(class2);

        assertFalse(classList.equals(otherList));
    }

    @Test
    @DisplayName("hashCode is consistent")
    void hashCode_isConsistent() {
        classList.add(class1);
        int hash1 = classList.hashCode();
        int hash2 = classList.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    @DisplayName("toString returns empty message for empty list")
    void toString_emptyList_returnsEmptyMessage() {
        String result = classList.toString();
        assertTrue(result.contains("No classes available"));
    }

    @Test
    @DisplayName("toString lists class names")
    void toString_nonEmptyList_listsClasses() {
        classList.add(class1);
        classList.add(class2);

        String result = classList.toString();
        assertTrue(result.contains("CS2103T T12"));
        assertTrue(result.contains("CS2103T T13"));
    }

    @Test
    @DisplayName("setClass updates an existing class")
    void setClass_validUpdate_success() {
        classList.add(class1);
        TuitionClass updatedClass = new TuitionClass(new ClassName("CS2103T T15"), tutor1);

        classList.setClass(class1, updatedClass);
        assertFalse(classList.contains(class1));
        assertTrue(classList.contains(updatedClass));
    }

    @Test
    @DisplayName("setClass throws when class not found")
    void setClass_nonExistentClass_throws() {
        TuitionClass updatedClass = new TuitionClass(new ClassName("CS2103T T15"), tutor1);
        assertThrows(ClassNotFoundException.class, () -> classList.setClass(class1, updatedClass));
    }

    @Test
    @DisplayName("setClass throws when resulting class duplicates existing entry")
    void setClass_duplicateClass_throws() {
        classList.add(class1);
        classList.add(class2);

        TuitionClass duplicateClass = new TuitionClass(new ClassName("CS2103T T13"), tutor1);
        assertThrows(DuplicateClassException.class, () -> classList.setClass(class1, duplicateClass));
    }
}
