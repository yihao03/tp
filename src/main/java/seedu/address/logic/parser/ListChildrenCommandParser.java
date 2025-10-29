package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ListChildrenCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListChildrenCommand object.
 */
public class ListChildrenCommandParser implements Parser<ListChildrenCommand> {

    private static final Logger logger = LogsCenter.getLogger(ListChildrenCommandParser.class);

    @Override
    public ListChildrenCommand parse(String args) throws ParseException {
        requireNonNull(args);
        logger.fine("Parsing ListChildrenCommand with args: " + args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!map.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListChildrenCommand.MESSAGE_USAGE));
        }

        String parentName = map.getValue(PREFIX_NAME).get().trim();

        if (parentName.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListChildrenCommand.MESSAGE_USAGE));
        }

        return new ListChildrenCommand(parentName);
    }
}
