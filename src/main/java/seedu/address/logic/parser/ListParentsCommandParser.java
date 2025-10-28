package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.address.logic.commands.ListParentsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListParentsCommand object.
 */
public class ListParentsCommandParser implements Parser<ListParentsCommand> {

    @Override
    public ListParentsCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(" " + args, PREFIX_NAME);

        if (!map.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListParentsCommand.MESSAGE_USAGE));
        }

        String childName = map.getValue(PREFIX_NAME).get().trim();

        if (childName.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListParentsCommand.MESSAGE_USAGE));
        }

        return new ListParentsCommand(childName);
    }
}
