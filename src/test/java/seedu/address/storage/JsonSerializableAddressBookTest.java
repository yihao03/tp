package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.classroom.ClassSession;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Student;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path CLASS_WITH_ATTENDANCE_FILE = TEST_DATA_FOLDER
            .resolve("classWithAttendanceAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_classWithAttendance_attendanceRestored() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(CLASS_WITH_ATTENDANCE_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Get the class and session
        TuitionClass mathClass = addressBook.getClassList().stream()
                .filter(c -> c.getClassName().equals("Math101"))
                .findFirst()
                .orElse(null);

        assertEquals("Math101", mathClass.getClassName());
        assertEquals(1, mathClass.getAllSessions().size());

        ClassSession session = mathClass.getAllSessions().get(0);
        assertEquals("Week 1", session.getSessionName());
        assertEquals(1, session.getAttendanceCount());

        // Verify attendance for specific students
        Student alice = (Student) addressBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Alice Student"))
                .findFirst()
                .orElse(null);
        Student bob = (Student) addressBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Bob Student"))
                .findFirst()
                .orElse(null);

        assertTrue(session.hasAttended(alice));
        assertTrue(!session.hasAttended(bob));
    }

    @Test
    public void toModelType_duplicateNames_allPersonsLoaded() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TEST_DATA_FOLDER.resolve("duplicateNamesAddressBook.json"),
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Verify all 4 persons are loaded (3 John Doe with different roles + 1 Jane Smith)
        assertEquals(4, addressBook.getPersonList().size());

        // Verify John Doe as STUDENT exists
        assertTrue(addressBook.getPersonList().stream()
                .anyMatch(p -> p.getName().fullName.equals("John Doe")
                        && p.getPersonType().name().equals("STUDENT")));

        // Verify John Doe as TUTOR exists
        assertTrue(addressBook.getPersonList().stream()
                .anyMatch(p -> p.getName().fullName.equals("John Doe")
                        && p.getPersonType().name().equals("TUTOR")));

        // Verify John Doe as PARENT exists
        assertTrue(addressBook.getPersonList().stream()
                .anyMatch(p -> p.getName().fullName.equals("John Doe")
                        && p.getPersonType().name().equals("PARENT")));
    }

    @Test
    public void toModelType_duplicateNames_classAssignmentCorrect() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TEST_DATA_FOLDER.resolve("duplicateNamesAddressBook.json"),
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        TuitionClass mathClass = addressBook.getClassList().stream()
                .filter(c -> c.getClassName().equals("Math101"))
                .findFirst()
                .orElse(null);

        // Verify tutor is John Doe TUTOR (not STUDENT or PARENT)
        assertEquals("John Doe", mathClass.getTutor().getName().fullName);
        assertEquals("92345678", mathClass.getTutor().getPhone().value);
        assertEquals("john.tutor@example.com", mathClass.getTutor().getEmail().value);

        // Verify students include John Doe STUDENT (not TUTOR or PARENT)
        assertEquals(2, mathClass.getStudents().size());
        Student johnStudent = mathClass.getStudents().stream()
                .filter(s -> s.getName().fullName.equals("John Doe"))
                .findFirst()
                .orElse(null);

        assertEquals("91234567", johnStudent.getPhone().value);
        assertEquals("john.student@example.com", johnStudent.getEmail().value);
    }

    @Test
    public void toModelType_duplicateNames_attendanceCorrect() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TEST_DATA_FOLDER.resolve("duplicateNamesAddressBook.json"),
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        TuitionClass mathClass = addressBook.getClassList().stream()
                .filter(c -> c.getClassName().equals("Math101"))
                .findFirst()
                .orElse(null);

        ClassSession session = mathClass.getAllSessions().get(0);

        // Find John Doe STUDENT specifically
        Student johnStudent = (Student) addressBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("John Doe")
                        && p.getPersonType().name().equals("STUDENT"))
                .findFirst()
                .orElse(null);

        Student janeStudent = (Student) addressBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Jane Smith"))
                .findFirst()
                .orElse(null);

        // Verify attendance is marked for correct John Doe (STUDENT)
        assertTrue(session.hasAttended(johnStudent));
        assertTrue(!session.hasAttended(janeStudent));
    }

    @Test
    public void toModelType_duplicateNames_parentChildRelationshipCorrect() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                TEST_DATA_FOLDER.resolve("duplicateNamesAddressBook.json"),
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Find John Doe PARENT specifically
        Parent johnParent = (Parent) addressBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("John Doe")
                        && p.getPersonType().name().equals("PARENT"))
                .findFirst()
                .orElse(null);

        // Find John Doe STUDENT specifically
        Student johnStudent = (Student) addressBook.getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("John Doe")
                        && p.getPersonType().name().equals("STUDENT"))
                .findFirst()
                .orElse(null);

        // Verify parent has the correct child (John Doe STUDENT, not TUTOR)
        assertEquals(1, johnParent.getChildren().size());
        assertTrue(johnParent.getChildren().contains(johnStudent));

        // Verify student has the correct parent (John Doe PARENT, not TUTOR)
        assertEquals(1, johnStudent.getParents().size());
        assertTrue(johnStudent.getParents().contains(johnParent));
    }

}
