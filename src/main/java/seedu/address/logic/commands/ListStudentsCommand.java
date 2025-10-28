package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Lists all students in a specific class.
 */
public class ListStudentsCommand extends Command {

    public static final String COMMAND_WORD = "liststudents";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all students in a specific class.\n"
            + "Parameters: " + PREFIX_CLASS + "CLASS_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_CLASS + "Math101";

    public static final String MESSAGE_SUCCESS = "Listed all students for class: %s";
    public static final String MESSAGE_CLASS_NOT_FOUND = "Class not found: %s";

    private final String className;

    /**
     * Creates a ListStudentsCommand to list all students for a specific class.
     *
     * @param className the name of the class whose students should be listed
     * @throws NullPointerException if className is null
     */
    public ListStudentsCommand(String className) {
        this.className = requireNonNull(className, "className cannot be null");
    }

    /**
     * Executes the list students command to display all students for the specified class.
     *
     * @param model the model containing the class list
     * @return the command result with the list of students
     * @throws CommandException if the class is not found
     * @throws NullPointerException if model is null
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model, "model cannot be null");

        List<TuitionClass> classList = model.getFilteredClassList();

        TuitionClass tuitionClass = classList.stream()
                .filter(c -> c.getClassName().equalsIgnoreCase(className))
                .findFirst()
                .orElse(null);

        if (tuitionClass == null) {
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, className));
        }

        List<Student> students = tuitionClass.getStudents();

        // Convert students to Person list for display
        List<Person> studentPersons = new ArrayList<>(students);

        // Update the left panel (PersonListPanel) only
        model.updateFilteredPersonList(person -> studentPersons.contains(person));

        String output;
        if (students.isEmpty()) {
            output = "[No students in this class]";
        } else {
            output = String.format("Displaying %d student(s) for class: %s", students.size(), className);
        }

        return new CommandResult(output, CommandResult.DisplayType.NONE);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ListStudentsCommand o)) {
            return false;
        }
        return className.equalsIgnoreCase(o.className);
    }

    @Override
    public int hashCode() {
        return className.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("ListStudentsCommand[className=%s]", className);
    }
}
