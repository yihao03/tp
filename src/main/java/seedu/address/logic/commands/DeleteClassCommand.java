package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;

/**
 * Deletes a class from TutBook.
 */
public class DeleteClassCommand extends Command {

    public static final String COMMAND_WORD = "deleteclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the specified class from TutBook.\n"
            + "Parameters: CLASS_NAME (can contain spaces)\n"
            + "Example: " + COMMAND_WORD + " Sec3-Math-A\n"
            + "Example: " + COMMAND_WORD + " Advanced Math";

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

        TuitionClass classToDelete = new TuitionClass(new ClassName(className));

        if (!model.hasClass(classToDelete)) {
            LOGGER.warning("Class not found: " + className);
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, className));
        }

        model.deleteClass(classToDelete);
        LOGGER.info("Successfully deleted class: " + className);

        return new CommandResult(String.format(MESSAGE_DELETE_CLASS_SUCCESS, className),
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
