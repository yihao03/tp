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
}
