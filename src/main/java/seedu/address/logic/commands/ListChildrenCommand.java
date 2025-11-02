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
 * Lists children of a specific parent.
 */
public class ListChildrenCommand extends Command {

    public static final String COMMAND_WORD = "childrenof";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists children of a specific parent.\n"
            + "Parameters: " + PREFIX_NAME + "PARENT_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "John Doe";

    public static final String MESSAGE_SUCCESS = "Listed children for parent: %s";
    public static final String MESSAGE_PARENT_NOT_FOUND = "Parent not found: %s";

    private static final Logger LOGGER = LogsCenter.getLogger(ListChildrenCommand.class);

    private final String parentName;

    /**
     * Creates a ListChildrenCommand to list children of a specific parent.
     */
    public ListChildrenCommand(String parentName) {
        requireNonNull(parentName);
        this.parentName = parentName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LOGGER.info("Executing ListChildrenCommand for parent: " + parentName);
        List<Person> personList = model.getPersonList();

        Parent parent = personList.stream()
                .filter(p -> p.getPersonType() == PersonType.PARENT)
                .filter(p -> p.getName().fullName.equalsIgnoreCase(parentName))
                .map(p -> (Parent) p)
                .findFirst()
                .orElse(null);

        if (parent == null) {
            LOGGER.warning("Parent not found: " + parentName);
            throw new CommandException(String.format(MESSAGE_PARENT_NOT_FOUND, parentName));
        }

        List<Student> children = parent.getChildren();

        if (children.isEmpty()) {
            LOGGER.info("Parent has no children: " + parentName);
            // Show empty list in UI
            model.updateFilteredPersonList(p -> false);
            return new CommandResult(String.format(MESSAGE_SUCCESS, parentName) + "\n[No children]");
        }

        // Filter to show only the children in the UI
        model.updateFilteredPersonList(children::contains);
        int childCount = children.size();
        LOGGER.info("Found " + childCount + " child(ren) for parent: " + parentName);

        return new CommandResult(String.format(MESSAGE_SUCCESS, parentName) + " (" + childCount + " shown)");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ListChildrenCommand)) {
            return false;
        }
        ListChildrenCommand o = (ListChildrenCommand) other;
        return parentName.equals(o.parentName);
    }
}
