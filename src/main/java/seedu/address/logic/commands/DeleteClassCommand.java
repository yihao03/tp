package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.TuitionClass;

/**
 * Deletes a class from TutBook.
 */
public class DeleteClassCommand extends Command {

    public static final String COMMAND_WORD = "deleteclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the specified class from TutBook.\n"
            + "Parameters: " + PREFIX_CLASS + "CLASS_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_CLASS + "Sec3-Math-A";

    public static final String MESSAGE_DELETE_CLASS_SUCCESS = "Deleted class: %1$s";
    public static final String MESSAGE_CLASS_NOT_FOUND = "Class %1$s does not exist";

    private static final Logger LOGGER = LogsCenter.getLogger(DeleteClassCommand.class);

    private final String className;

    /**
     * Creates a DeleteClassCommand to delete the specified class.
     * @param className The name of the class to be deleted
     */
    public DeleteClassCommand(String className) {
        requireNonNull(className);
        this.className = className;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LOGGER.info("Executing DeleteClassCommand for class: " + className);

        // Find the actual class from the model (not a new empty instance)
        TuitionClass classToDelete = model.getFilteredClassList().stream()
                .filter(c -> c.getName().value.equalsIgnoreCase(className))
                .findFirst()
                .orElse(null);

        if (classToDelete == null) {
            LOGGER.warning("Class not found: " + className);
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, className));
        }

        model.deleteClass(classToDelete);
        LOGGER.info("Successfully deleted class: " + className);

        // Update the filtered person list to refresh UI display of students and tutors
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_DELETE_CLASS_SUCCESS, classToDelete.getName()),
                CommandResult.DisplayType.CLASSES);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteClassCommand)) {
            return false;
        }

        DeleteClassCommand otherCommand = (DeleteClassCommand) other;
        return className.equals(otherCommand.className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }
}
