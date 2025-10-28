package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    private static final Logger LOGGER = LogsCenter.getLogger(ClearCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        LOGGER.warning("Executing ClearCommand - clearing entire address book");

        model.setAddressBook(new AddressBook());
        LOGGER.info("Address book cleared successfully");

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
