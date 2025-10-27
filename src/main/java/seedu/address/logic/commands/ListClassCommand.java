package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Student;

/**
 * Lists all classes with their enrolled students in the address book to the user.
 */
public class ListClassCommand extends Command {

    public static final String COMMAND_WORD = "listclass";
    public static final String MESSAGE_SUCCESS = "Listed all classes with their students";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        List<TuitionClass> classList = model.getFilteredClassList();

        String output;
        if (classList.isEmpty()) {
            output = MESSAGE_SUCCESS + "\n[No classes]";
        } else {
            output = String.format("Displaying %d class(es)", classList.size());
        }

        // The filtered class list is already being displayed, so no need to set it again
        return new CommandResult(output, CommandResult.DisplayType.CLASSES);
    }

    /**
     * Formats a tuition class with its enrolled students.
     */
    private String formatClassWithStudents(TuitionClass tuitionClass) {
        String className = tuitionClass.getClassName();

        String tutorLabel = tuitionClass.isAssignedToTutor()
            ? tuitionClass.getTutor().getName().fullName
            : "Unassigned";

        List<Student> students = tuitionClass.getStudents();
        String studentsLabel = students.isEmpty()
                ? "[No students]"
                : students.stream()
                        .map(s -> s.getName().fullName)
                        .collect(Collectors.joining(", "));

        return String.format("%s (Tutor: %s): %s", className, tutorLabel, studentsLabel);
    }
}
