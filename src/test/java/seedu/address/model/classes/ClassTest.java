package seedu.address.model.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Student;

/**
 * Tests for Class.
 */
public class ClassTest {

    @Test
    public void constructor_validInputs_success() {
        Class classObj = new Class("CS2103T", "SE");
        assertEquals("CS2103T SE", classObj.getClassName());
        assertNull(classObj.getTutor());
        assertTrue(classObj.getStudents().isEmpty());
    }

    @Test
    public void getClassName_returnsCorrectFormat() {
        Class classObj = new Class("CS2103T", "Software Engineering");
        assertEquals("CS2103T Software Engineering", classObj.getClassName());
    }

    @Test
    public void getTutor_initiallyNull() {
        Class classObj = new Class("CS2103T", "SE");
        assertNull(classObj.getTutor());
    }

    @Test
    public void getStudents_returnsEmptyListInitially() {
        Class classObj = new Class("CS2103T", "SE");
        ArrayList<Student> students = classObj.getStudents();
        assertTrue(students.isEmpty());
    }

    @Test
    public void getStudents_returnsMutableList() {
        Class classObj = new Class("CS2103T", "SE");
        ArrayList<Student> students = classObj.getStudents();
        assertTrue(students.isEmpty());
        // Verify the returned list is the internal list by checking size after addStudent
        assertEquals(0, students.size());
    }

    @Test
    public void isSameClass_sameObject_returnsTrue() {
        Class classObj = new Class("CS2103T", "SE");
        assertTrue(classObj.isSameClass(classObj));
    }

    @Test
    public void isSameClass_null_returnsFalse() {
        Class classObj = new Class("CS2103T", "SE");
        assertFalse(classObj.isSameClass(null));
    }

    @Test
    public void isSameClass_sameClassName_returnsTrue() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2103T", "SE");
        assertTrue(class1.isSameClass(class2));
    }

    @Test
    public void isSameClass_differentClassName_returnsFalse() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2040C", "Algo");
        assertFalse(class1.isSameClass(class2));
    }

    @Test
    public void isSameClass_differentSubject_returnsFalse() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2103T", "Software Engineering");
        assertFalse(class1.isSameClass(class2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Class classObj = new Class("CS2103T", "SE");
        assertEquals(classObj, classObj);
    }

    @Test
    public void equals_null_returnsFalse() {
        Class classObj = new Class("CS2103T", "SE");
        assertNotEquals(null, classObj);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Class classObj = new Class("CS2103T", "SE");
        assertNotEquals("CS2103T SE", classObj);
    }

    @Test
    public void equals_sameClassName_returnsTrue() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2103T", "SE");
        assertEquals(class1, class2);
    }

    @Test
    public void equals_differentClassName_returnsFalse() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2040C", "Algo");
        assertNotEquals(class1, class2);
    }

    @Test
    public void hashCode_sameClassName_returnsSameHashCode() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2103T", "SE");
        assertEquals(class1.hashCode(), class2.hashCode());
    }

    @Test
    public void hashCode_differentClassName_returnsDifferentHashCode() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2040C", "Algo");
        assertNotEquals(class1.hashCode(), class2.hashCode());
    }

    @Test
    public void toString_returnsClassName() {
        Class classObj = new Class("CS2103T", "SE");
        assertEquals("CS2103T", classObj.toString());
    }

    @Test
    public void toString_differentClasses_returnsDifferentStrings() {
        Class class1 = new Class("CS2103T", "SE");
        Class class2 = new Class("CS2040C", "Algo");
        assertNotEquals(class1.toString(), class2.toString());
    }
}
