package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;

/**
 * Removes a student or tutor from a tuition class.
 */
public class UnjoinClassCommand extends Command {

    public static final String COMMAND_WORD = "unjoin";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a student or tutor from a class.\n"
            + "Parameters: "
            + PREFIX_NAME + "PERSON_NAME "
            + PREFIX_CLASS + "CLASS_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_CLASS + "Math 101";

    public static final String MESSAGE_SUCCESS = "Removed %s from class: %s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "Person not found: %s";
    public static final String MESSAGE_CLASS_NOT_FOUND = "Class not found: %s";
    public static final String MESSAGE_NOT_IN_CLASS = "%s is not in class %s";
    public static final String MESSAGE_PARENT_CANNOT_BE_IN_CLASS = "Parent cannot be removed from class";

    private static final Logger LOGGER = LogsCenter.getLogger(UnjoinClassCommand.class);

    private final String personName;
    private final ClassName className;

    /**
     * Creates a RemoveFromClassCommand to remove the specified person from the
     * specified class.
     */
    public UnjoinClassCommand(String personName, ClassName className) {
        requireAllNonNull(personName, className);
        this.personName = personName;
        this.className = className;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LOGGER.info("Executing UnjoinClassCommand for person: " + personName + " and class: " + className);

        List<Person> personList = model.getFilteredPersonList();
        List<TuitionClass> classList = model.getFilteredClassList();

        // Find the person
        Person person = personList.stream()
                .filter(p -> p.getName().fullName.equalsIgnoreCase(personName))
                .findFirst()
                .orElseThrow(() -> {
                    LOGGER.warning("Person not found: " + personName);
                    return new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, personName));
                });
        String actualName = person.getName().toString();

        // Check if person is a parent
        if (person.getPersonType() == PersonType.PARENT) {
            LOGGER.warning("Attempted to remove parent from class: " + actualName);
            throw new CommandException(MESSAGE_PARENT_CANNOT_BE_IN_CLASS);
        }

        // Find the class
        TuitionClass tuitionClass = classList.stream()
                .filter(tc -> tc.getName().equals(className))
                .findFirst()
                .orElseThrow(() -> {
                    LOGGER.warning("Class not found: " + className);
                    return new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, className.value));
                });

        // Remove student or tutor from class
        if (person.getPersonType() == PersonType.STUDENT) {
            Student student = (Student) person;
            if (!tuitionClass.hasStudent(student)) {
                LOGGER.warning("Student not in class: " + actualName + " in " + className);
                throw new CommandException(String.format(MESSAGE_NOT_IN_CLASS, personName, className.value));
            }
            tuitionClass.removeStudent(student);
            student.unjoin(tuitionClass);
            LOGGER.info("Successfully removed student " + actualName + " from class " + className);
        } else if (person.getPersonType() == PersonType.TUTOR) {
            Tutor tutor = (Tutor) person;
            if (!tuitionClass.hasTutor(tutor)) {
                LOGGER.warning("Tutor not assigned to class: " + actualName + " in " + className);
                throw new CommandException(String.format(MESSAGE_NOT_IN_CLASS, personName, className.value));
            }
            tuitionClass.removeTutor(tutor);
            tutor.unjoin(tuitionClass);
            LOGGER.info("Successfully removed tutor " + actualName + " from class " + className);
        }

        // Force ObservableList to update by calling setClass
        model.setClass(tuitionClass, tuitionClass);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredClassList(Model.PREDICATE_SHOW_ALL_CLASSES);
        return new CommandResult(String.format(MESSAGE_SUCCESS, actualName, className.value),
                CommandResult.DisplayType.CLASSES);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnjoinClassCommand)) {
            return false;
        }

        UnjoinClassCommand otherCommand = (UnjoinClassCommand) other;
        return personName.equals(otherCommand.personName)
                && className.equals(otherCommand.className);
    }
}
