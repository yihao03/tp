package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                        .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                        .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                        new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                        new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                        new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                        + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void execute_editStudentWithoutTypeChange_maintainsBidirectionalRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create parent and student
        Person parent = new PersonBuilder().withName("Parent").withPhone("91234567")
                .withPersonType(PersonType.PARENT).build();
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(parent);
        modelWithRelationships.addPerson(student);

        // Establish relationship
        Student studentCast = (Student) student;
        Parent parentCast = (Parent) parent;
        studentCast.addParent(parentCast);

        // Edit student's phone
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("99999999").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited student
        Person editedStudent = modelWithRelationships.getFilteredPersonList().get(1);

        // Verify bidirectional relationship maintained
        assertTrue(((Student) editedStudent).getParents().contains(parent));
        assertTrue(((Parent) parent).getChildren().contains(editedStudent));
    }

    @Test
    public void execute_editParentWithoutTypeChange_maintainsBidirectionalRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create parent and student
        Person parent = new PersonBuilder().withName("Parent").withPhone("91234567")
                .withPersonType(PersonType.PARENT).build();
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(parent);
        modelWithRelationships.addPerson(student);

        // Establish relationship
        Parent parentCast = (Parent) parent;
        Student studentCast = (Student) student;
        parentCast.addChild(studentCast);

        // Edit parent's phone
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("99999999").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited parent
        Person editedParent = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify bidirectional relationship maintained
        assertTrue(((Parent) editedParent).getChildren().contains(student));
        assertTrue(((Student) student).getParents().contains(editedParent));
    }

    @Test
    public void execute_changeStudentToTutor_removesRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create parent and student
        Person parent = new PersonBuilder().withName("Parent").withPhone("91234567")
                .withPersonType(PersonType.PARENT).build();
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(parent);
        modelWithRelationships.addPerson(student);

        // Establish relationship
        Student studentCast = (Student) student;
        Parent parentCast = (Parent) parent;
        studentCast.addParent(parentCast);

        // Change student to tutor
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPersonType(PersonType.TUTOR).build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get parent from model (it should still be a Parent)
        Person parentFromModel = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify parent no longer has student as child
        assertEquals(0, ((Parent) parentFromModel).getChildren().size());
    }

    @Test
    public void execute_editTutor_noRelationshipLogic() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a tutor
        Person tutor = new PersonBuilder().withName("Tutor").withPhone("91234567")
                .withPersonType(PersonType.TUTOR).build();

        modelWithRelationships.addPerson(tutor);

        // Edit tutor's phone
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("99999999").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited tutor
        Person editedTutor = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify tutor was edited (no relationship logic should execute for tutors)
        assertEquals(PersonType.TUTOR, editedTutor.getPersonType());
    }

    @Test
    public void execute_changeParentToTutor_removesRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create parent and student
        Person parent = new PersonBuilder().withName("Parent").withPhone("91234567")
                .withPersonType(PersonType.PARENT).build();
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(parent);
        modelWithRelationships.addPerson(student);

        // Establish relationship
        Parent parentCast = (Parent) parent;
        Student studentCast = (Student) student;
        parentCast.addChild(studentCast);

        // Change parent to tutor
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPersonType(PersonType.TUTOR).build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get student from model (it should still be a Student)
        Person studentFromModel = modelWithRelationships.getFilteredPersonList().get(1);

        // Verify student no longer has parent
        assertEquals(0, ((Student) studentFromModel).getParents().size());
    }

    @Test
    public void execute_editStudentWithoutTypeChange_maintainsTuitionClassRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create student and tuition class
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(student);

        // Create tuition class and establish relationship
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        Student studentCast = (Student) student;
        tuitionClass.addStudent(studentCast);

        // Edit student's phone
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("99999999").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited student
        Person editedStudent = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify bidirectional relationship maintained
        assertTrue(((Student) editedStudent).getTuitionClasses().contains(tuitionClass));
        assertTrue(tuitionClass.getStudents().contains(editedStudent));
    }

    @Test
    public void execute_editTutorWithoutTypeChange_maintainsTuitionClassRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create tutor and tuition class
        Person tutor = new PersonBuilder().withName("Tutor").withPhone("91234567")
                .withPersonType(PersonType.TUTOR).build();

        modelWithRelationships.addPerson(tutor);

        // Create tuition class and establish relationship
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        Tutor tutorCast = (Tutor) tutor;
        tuitionClass.setTutor(tutorCast);

        // Edit tutor's phone
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone("99999999").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited tutor
        Person editedTutor = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify bidirectional relationship maintained
        assertTrue(((Tutor) editedTutor).getTuitionClasses().contains(tuitionClass));
        assertEquals(editedTutor, tuitionClass.getTutor());
    }

    @Test
    public void execute_changeStudentToParent_removesTuitionClassRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create student and tuition class
        Person student = new PersonBuilder().withName("Student").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(student);

        // Create tuition class and establish relationship
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        Student studentCast = (Student) student;
        tuitionClass.addStudent(studentCast);

        // Change student to parent
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPersonType(PersonType.PARENT).build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Verify tuition class no longer has student
        assertEquals(0, tuitionClass.getStudents().size());
    }

    @Test
    public void execute_changeTutorToStudent_removesTuitionClassRelationships() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create tutor and tuition class
        Person tutor = new PersonBuilder().withName("Tutor").withPhone("91234567")
                .withPersonType(PersonType.TUTOR).build();

        modelWithRelationships.addPerson(tutor);

        // Create tuition class and establish relationship
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        Tutor tutorCast = (Tutor) tutor;
        tuitionClass.setTutor(tutorCast);

        // Change tutor to student
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPersonType(PersonType.STUDENT).build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Verify tuition class no longer has tutor
        assertFalse(tuitionClass.isAssignedToTutor());
    }

    @Test
    public void execute_editStudentName_maintainsSessionAttendance() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create student
        Person student = new PersonBuilder().withName("Emma Yeoh").withPhone("98765432")
                .withPersonType(PersonType.STUDENT).build();

        modelWithRelationships.addPerson(student);

        // Create tuition class, add student, and create session
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        modelWithRelationships.addClass(tuitionClass);
        Student studentCast = (Student) student;
        tuitionClass.addStudent(studentCast);

        // Create session and mark student as present
        tuitionClass.addSession("Session 1",
                java.time.LocalDateTime.of(2024, 3, 15, 14, 30), "COM1");
        seedu.address.model.classroom.ClassSession session = tuitionClass.getAllSessions().get(0);
        session.markPresent(studentCast);

        // Verify student is present
        assertTrue(session.hasAttended(studentCast));
        assertEquals(1, session.getAttendanceCount());

        // Edit student's name
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Emma").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited student
        Person editedStudent = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify edited student is in tuition class
        assertTrue(tuitionClass.getStudents().contains(editedStudent));

        // Verify session attendance is maintained for edited student
        assertTrue(session.hasAttended((Student) editedStudent));
        assertEquals(1, session.getAttendanceCount());

        // Verify old student is no longer in attendance record
        assertFalse(session.getAttendanceRecord().containsKey(studentCast));

        // Verify new student is in attendance record
        assertTrue(session.getAttendanceRecord().containsKey((Student) editedStudent));
    }

    @Test
    public void execute_editTutorName_updatesTuitionClassTutorProperty() throws Exception {
        Model modelWithRelationships = new ModelManager(new AddressBook(), new UserPrefs());

        // Create tutor Jonathan
        Person tutor = new PersonBuilder().withName("Jonathan").withPhone("98765432")
                .withPersonType(PersonType.TUTOR).build();

        modelWithRelationships.addPerson(tutor);

        // Create tuition class and assign Jonathan as tutor
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"));
        modelWithRelationships.addClass(tuitionClass);
        Tutor tutorCast = (Tutor) tutor;
        tuitionClass.setTutor(tutorCast);

        // Verify Jonathan is the tutor
        assertTrue(tuitionClass.isAssignedToTutor());
        assertEquals("Jonathan", tuitionClass.getTutor().getName().fullName);
        assertEquals("Jonathan", tuitionClass.getTutorProperty().get());

        // Edit tutor's name to Jay
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName("Jay").build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(1), descriptor);

        editCommand.execute(modelWithRelationships);

        // Get edited tutor
        Person editedTutor = modelWithRelationships.getFilteredPersonList().get(0);

        // Verify Jay is the new tutor
        assertTrue(tuitionClass.isAssignedToTutor());
        assertEquals("Jay", editedTutor.getName().fullName);
        assertEquals("Jay", tuitionClass.getTutor().getName().fullName);
        assertEquals("Jay", tuitionClass.getTutorProperty().get()); // This should pass if the bug is fixed
    }

}
