package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLASSES;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;

/**
 * Edits the name of an existing class in TutBook.
 */
public class EditClassCommand extends Command {

    public static final String COMMAND_WORD = "editclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the name of an existing class.\n"
            + "Parameters: o/OLD_CLASS_NAME c/NEW_CLASS_NAME\n"
            + "  o/ specifies the old class name\n"
            + "  c/ specifies the new class name\n"
            + "  Class names can contain spaces.\n"
            + "Example: " + COMMAND_WORD + " o/Sec2-Math-A c/Sec3-Math-A\n"
            + "Example: " + COMMAND_WORD + " o/Advanced Math c/Honors Mathematics";

    public static final String MESSAGE_EDIT_CLASS_SUCCESS = "Edited class from %1$s to %2$s";
    public static final String MESSAGE_CLASS_NOT_FOUND = "Class %1$s does not exist";
    public static final String MESSAGE_DUPLICATE_CLASS = "Class %1$s already exists";

    private static final Logger LOGGER = LogsCenter.getLogger(EditClassCommand.class);

    private final String oldClassName;
    private final String newClassName;

    /**
     * Creates an EditClassCommand to edit the specified class.
     *
     * @param oldClassName The current name of the class to be edited
     * @param newClassName The new name for the class
     */
    public EditClassCommand(String oldClassName, String newClassName) {
        requireNonNull(oldClassName);
        requireNonNull(newClassName);
        this.oldClassName = oldClassName;
        this.newClassName = newClassName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LOGGER.info("Executing EditClassCommand from: " + oldClassName + " to: " + newClassName);

        TuitionClass oldClass = model.getFilteredClassList().stream()
                .filter(c -> c.getName().value.equalsIgnoreCase(oldClassName))
                .findFirst()
                .orElse(null);

        if (oldClass == null) {
            LOGGER.warning("Class not found: " + oldClassName);
            throw new CommandException(String.format(MESSAGE_CLASS_NOT_FOUND, oldClassName));
        }

        TuitionClass newClass = new TuitionClass(new ClassName(newClassName));

        if (!oldClass.isSameClass(newClass) && model.hasClass(newClass)) {
            LOGGER.warning("Duplicate class name: " + newClassName);
            throw new CommandException(String.format(MESSAGE_DUPLICATE_CLASS, newClassName));
        }

        newClass.transferDetailsFromClass(oldClass);

        model.setClass(oldClass, newClass);
        model.updateFilteredClassList(PREDICATE_SHOW_ALL_CLASSES);
        LOGGER.info("Successfully edited class from: " + oldClass.getName() + " to: " + newClassName);

        return new CommandResult(String.format(MESSAGE_EDIT_CLASS_SUCCESS, oldClass.getName(), newClassName),
                CommandResult.DisplayType.CLASSES);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditClassCommand)) {
            return false;
        }

        EditClassCommand otherCommand = (EditClassCommand) other;
        return oldClassName.equals(otherCommand.oldClassName)
                && newClassName.equals(otherCommand.newClassName);
    }

    @Override
    public int hashCode() {
        return oldClassName.hashCode() + newClassName.hashCode();
    }
}
