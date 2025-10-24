package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.classroom.ClassSession;
import seedu.address.model.classroom.TuitionClass;

/**
 * Views a session's details.
 */
public class ViewSessionCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": View a session's details. "
            + "Parameters: "
            + PREFIX_CLASS + "Class "
            + PREFIX_SESSION + "Session "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASS + "Maths "
            + PREFIX_SESSION + "1 ";

    public static final String MESSAGE_SUCCESS = "Session details: %s\n";
    public static final String MESSAGE_CLASS_NOT_FOUND = "The specified class does not exist.";
    public static final String MESSAGE_SESSION_NOT_FOUND = "The specified session cannot be found. ";

    private static Logger logger = Logger.getLogger(ViewSessionCommand.class.getName());
    private final String className;
    private final String sessionName;

    /**
     * Creates an ViewSessionCommand to view the specified session.
     */
    public ViewSessionCommand(String className, String sessionName) {
        requireNonNull(className);
        requireNonNull(sessionName);
        this.className = className;
        this.sessionName = sessionName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        TuitionClass classToView = model.getClassByName(this.className);

        if (classToView == null) {
            logger.log(Level.WARNING, "Class not found: " + this.className);
            throw new CommandException(MESSAGE_CLASS_NOT_FOUND);
        }

        Optional<ClassSession> toView = classToView.getSession(sessionName);
        if (toView.isEmpty()) {
            throw new CommandException(MESSAGE_SESSION_NOT_FOUND);
        }

        logger.log(Level.INFO, "Viewing session: " + sessionName + " of class: " + className);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toView.get().getSessionDetails()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewSessionCommand)) {
            return false;
        }

        ViewSessionCommand otherViewCommand = (ViewSessionCommand) other;
        return this.className.equals(otherViewCommand.className)
                && this.sessionName.equals(otherViewCommand.sessionName);
    }

    /**
     * Returns a string representation of this ViewSessionCommand.
     *
     * @return A string representation of this command.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("className", className)
                .add("sessionName", sessionName)
                .toString();
    }
}
