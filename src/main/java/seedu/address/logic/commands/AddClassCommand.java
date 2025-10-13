package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classes.Class;

/**
 * Adds a class to the address book.
 */
public class AddClassCommand extends Command {

    public static final String COMMAND_WORD = "addclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a class to the address book. " + "Parameters: "
            + PREFIX_CLASSNAME + "CLASS_NAME "
            + PREFIX_SUBJECT + "SUBJECT\n"
            + "Example: "
            + COMMAND_WORD + " "
            + PREFIX_CLASSNAME + "CS2103T "
            + PREFIX_SUBJECT + "Software Engineering";

    public static final String MESSAGE_SUCCESS = "New class added: %1$s";
    public static final String MESSAGE_DUPLICATE_CLASS = "This class already exists in the address book";

    private final Class toAdd;

    /**
     * Creates an AddClassCommand to add the specified {@code Class}
     */
    public AddClassCommand(Class classToAdd) {
        requireNonNull(classToAdd);
        toAdd = classToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasClass(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLASS);
        }

        model.addClass(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddClassCommand)) {
            return false;
        }

        AddClassCommand otherAddClassCommand = (AddClassCommand) other;
        return toAdd.equals(otherAddClassCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
