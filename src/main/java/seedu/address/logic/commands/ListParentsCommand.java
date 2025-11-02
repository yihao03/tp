package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;
import seedu.address.model.person.Student;

/**
 * Lists parents of a specific child.
 */
public class ListParentsCommand extends Command {

    public static final String COMMAND_WORD = "parentsof";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists parents of a specific child.\n"
            + "Parameters: " + PREFIX_NAME + "CHILD_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "Jane Smith";

    public static final String MESSAGE_SUCCESS = "Listed parents for child: %s";
    public static final String MESSAGE_CHILD_NOT_FOUND = "Child not found: %s";

    private static final Logger LOGGER = LogsCenter.getLogger(ListParentsCommand.class);

    private final String childName;

    /**
     * Creates a ListParentsCommand to list parents of a specific child.
     *
     * @param childName Name of the child whose parents to list
     */
    public ListParentsCommand(String childName) {
        requireNonNull(childName);
        this.childName = childName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LOGGER.info("Executing ListParentsCommand" + (childName != null ? " for child: " + childName : ""));
        List<Person> personList = model.getPersonList();

        Student child = personList.stream()
                .filter(p -> p.getPersonType() == PersonType.STUDENT)
                .filter(p -> p.getName().fullName.equalsIgnoreCase(childName))
                .map(p -> (Student) p)
                .findFirst()
                .orElse(null);

        if (child == null) {
            LOGGER.warning("Child not found: " + childName);
            throw new CommandException(String.format(MESSAGE_CHILD_NOT_FOUND, childName));
        }

        List<Parent> parents = child.getParents();

        if (parents.isEmpty()) {
            LOGGER.info("Child has no parents: " + childName);
            // Show empty list in UI
            model.updateFilteredPersonList(p -> false);
            return new CommandResult(String.format(MESSAGE_SUCCESS, childName) + "\n[No parents]");
        }

        // Filter to show only the parents in the UI
        model.updateFilteredPersonList(parents::contains);
        int parentCount = parents.size();
        LOGGER.info("Found " + parentCount + " parent(s) for child: " + childName);

        return new CommandResult(String.format(MESSAGE_SUCCESS, childName) + " (" + parentCount + " shown)");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ListParentsCommand)) {
            return false;
        }
        ListParentsCommand o = (ListParentsCommand) other;
        return childName.equals(o.childName);
    }
}
