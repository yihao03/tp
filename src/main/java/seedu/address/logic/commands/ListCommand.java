package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    private static final Logger LOGGER = LogsCenter.getLogger(ListCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        LOGGER.info("Executing ListCommand - showing all persons");

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        int personCount = model.getFilteredPersonList().size();
        LOGGER.info("Listed " + personCount + " persons");

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
