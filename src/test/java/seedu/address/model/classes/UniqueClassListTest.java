package seedu.address.model.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.ClassNotFoundException;
import seedu.address.model.person.exceptions.DuplicateClassException;

/**
 * Tests for UniqueClassList.
 */
public class UniqueClassListTest {

    private final UniqueClassList uniqueClassList = new UniqueClassList();

    private static Class cls(String name, String subject) {
        return new Class(name, subject);
    }

    @Test
    public void contains_nullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.contains(null));
    }

    @Test
    public void contains_classNotInList_returnsFalse() {
        assertFalse(uniqueClassList.contains(cls("CS2103T", "SE")));
    }

    @Test
    public void contains_classInList_returnsTrue() {
        Class c = cls("CS2103T", "SE");
        uniqueClassList.add(c);
        assertTrue(uniqueClassList.contains(c));
    }

    @Test
    public void contains_sameIdentityFieldsInList_returnsTrue() {
        Class c = cls("CS2103T", "SE");
        uniqueClassList.add(c);
        // same identity ignoring case
        assertTrue(uniqueClassList.contains(cls("cs2103t", "se")));
        // different subject -> not same identity
        assertFalse(uniqueClassList.contains(cls("CS2103T", "SWE")));
    }

    @Test
    public void add_nullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.add(null));
    }

    @Test
    public void add_duplicateClass_throwsDuplicateClassException() {
        Class c = cls("CS2103T", "SE");
        uniqueClassList.add(c);
        assertThrows(DuplicateClassException.class, () -> uniqueClassList.add(cls("cs2103t", "se")));
    }

    @Test
    public void setClass_nullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.setClass(null, cls("A", "B")));
    }

    @Test
    public void setClass_nullEdited_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.setClass(cls("A", "B"), null));
    }

    @Test
    public void setClass_targetNotInList_throwsClassNotFoundException() {
        assertThrows(ClassNotFoundException.class, () -> uniqueClassList.setClass(cls("A", "B"), cls("C", "D")));
    }

    @Test
    public void setClass_editedClassIsSameIdentity_success() {
        Class original = cls("CS2103T", "SE");
        uniqueClassList.add(original);
        Class editedSame = cls("cs2103t", "se");
        uniqueClassList.setClass(original, editedSame);
        assertTrue(uniqueClassList.contains(editedSame));
    }

    @Test
    public void setClass_editedClassHasDifferentIdentity_success() {
        Class original = cls("CS2103T", "SE");
        uniqueClassList.add(original);
        Class edited = cls("CS2040C", "Algo");
        uniqueClassList.setClass(original, edited);
        assertTrue(uniqueClassList.contains(edited));
        assertFalse(uniqueClassList.contains(original));
    }

    @Test
    public void setClass_editedClassHasNonUniqueIdentity_throwsDuplicateClassException() {
        Class a = cls("CS2103T", "SE");
        Class b = cls("CS2103T", "SWE"); // different identity
        Class duplicateOfB = cls("cs2103t", "swe");
        uniqueClassList.add(a);
        uniqueClassList.add(b);
        assertThrows(DuplicateClassException.class, () -> uniqueClassList.setClass(a, duplicateOfB));
    }

    @Test
    public void remove_nullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.remove(null));
    }

    @Test
    public void remove_classDoesNotExist_throwsClassNotFoundException() {
        assertThrows(ClassNotFoundException.class, () -> uniqueClassList.remove(cls("X", "Y")));
    }

    @Test
    public void remove_existingClass_removesClass() {
        Class c = cls("CS2103T", "SE");
        uniqueClassList.add(c);
        uniqueClassList.remove(c);
        assertFalse(uniqueClassList.contains(c));
    }

    @Test
    public void setClasses_nullUniqueClassList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.setClasses((UniqueClassList) null));
    }

    @Test
    public void setClasses_uniqueClassList_replacesOwnList() {
        Class newClass = cls("CS2040C", "Algo");
        UniqueClassList other = new UniqueClassList();
        other.add(newClass);
        uniqueClassList.setClasses(other);
        assertTrue(uniqueClassList.contains(newClass));
    }

    @Test
    public void setClasses_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueClassList.setClasses((List<Class>) null));
    }

    @Test
    public void setClasses_list_replacesOwnList() {
        Class newClass = cls("CS2100", "Arch");
        uniqueClassList.setClasses(Collections.singletonList(newClass));
        assertTrue(uniqueClassList.contains(newClass));
    }

    @Test
    public void setClasses_listWithDuplicates_throwsDuplicateClassException() {
        Class a = cls("CS2103T", "SE");
        Class duplicateA = cls("cs2103t", "se");
        List<Class> listWithDuplicates = Arrays.asList(a, duplicateA);
        assertThrows(DuplicateClassException.class, () -> uniqueClassList.setClasses(listWithDuplicates));
    }

    @Test
    public void asUnmodifiableObservableList_modify_throwsUnsupportedOperationException() {
        Class c = cls("CS2103T", "SE");
        uniqueClassList.add(c);
        ObservableList<Class> view = uniqueClassList.asUnmodifiableObservableList();
        assertThrows(UnsupportedOperationException.class, () -> view.remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueClassList.asUnmodifiableObservableList().toString(), uniqueClassList.toString());
    }

    @Test
    public void iterator_iteratesOverClasses() {
        Class a = cls("CS2103T", "SE");
        Class b = cls("CS2040C", "Algo");
        uniqueClassList.add(a);
        uniqueClassList.add(b);

        int count = 0;
        for (Class c : uniqueClassList) {
            count++;
            assertTrue(c.equals(a) || c.equals(b));
        }
        assertEquals(2, count);
    }

    @Test
    public void hashCode_sameContent_returnsSameHashCode() {
        Class a = cls("CS2103T", "SE");
        UniqueClassList list1 = new UniqueClassList();
        UniqueClassList list2 = new UniqueClassList();

        list1.add(a);
        list2.add(a);

        assertEquals(list1.hashCode(), list2.hashCode());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(uniqueClassList.equals(uniqueClassList));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(uniqueClassList.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(uniqueClassList.equals("string"));
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        Class a = cls("CS2103T", "SE");
        uniqueClassList.add(a);

        UniqueClassList otherList = new UniqueClassList();
        assertFalse(uniqueClassList.equals(otherList));
    }

    @Test
    public void equals_sameContent_returnsTrue() {
        Class a = cls("CS2103T", "SE");
        uniqueClassList.add(a);

        UniqueClassList otherList = new UniqueClassList();
        otherList.add(a);
        assertTrue(uniqueClassList.equals(otherList));
    }
}
