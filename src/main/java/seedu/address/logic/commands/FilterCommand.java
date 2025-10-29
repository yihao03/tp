package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonType;

/**
 * Filters persons by role (Student / Tutor / Parent).
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters contacts by role.\n"
            + "Parameters: ro/STUDENT|TUTOR|PARENT\n"
            + "Example: " + COMMAND_WORD + " ro/student";

    public static final String MESSAGE_SUCCESS = "Filtered by role: %s (%d shown)";
    public static final String MESSAGE_EMPTY_AFTER_FILTER = "No contacts match role: %s";

    private static final Logger LOGGER = LogsCenter.getLogger(FilterCommand.class);

    private final PersonType role;
    private final Predicate<Person> predicate;

    /**
     * Creates a filter command that shows persons whose concrete type matches {@code role}.
     */
    public FilterCommand(PersonType role) {
        this.role = requireNonNull(role);
        this.predicate = person -> person.getPersonType() == role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LOGGER.info("Executing FilterCommand for role: " + role.name().toLowerCase());

        model.updateFilteredPersonList(predicate);
        int size = model.getFilteredPersonList().size();
        LOGGER.info("Filter result: " + size + " persons found for role " + role.name().toLowerCase());

        if (size == 0) {
            // Keep the filter applied (so UI shows empty list), but tell user clearly.
            return new CommandResult(String.format(MESSAGE_EMPTY_AFTER_FILTER, role.name().toLowerCase()));
        }

        // Standard feedback includes count + keeps current filtered list in UI
        return new CommandResult(String.format(MESSAGE_SUCCESS, role.name().toLowerCase(), size));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FilterCommand)) {
            return false;
        }
        FilterCommand o = (FilterCommand) other;
        return role == o.role;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "{role=" + role + "}";
    }
}
