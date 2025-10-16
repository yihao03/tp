package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddClassCommand;
import seedu.address.logic.commands.JoinClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class JoinClassCommandParser implements Parser<JoinClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public JoinClassCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        String cmd = JoinClassCommand.COMMAND_WORD;

        if (trimmed.regionMatches(true, 0, cmd, 0, cmd.length())) {
            trimmed = trimmed.substring(cmd.length()).trim();
        }

        ArgumentMultimap map = ArgumentTokenizer.tokenize(" " + trimmed, PREFIX_CLASS_NAME, PREFIX_NAME);

        if (!arePrefixesPresent(map, PREFIX_CLASS_NAME, PREFIX_NAME) || !map.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassCommand.MESSAGE_USAGE));
        }

        // Only parse tutor name if present
        if (!map.getValue(PREFIX_CLASS_NAME).isPresent() || !map.getValue(PREFIX_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    JoinClassCommand.MESSAGE_USAGE));
        }

        return new JoinClassCommand(map.getValue(PREFIX_NAME).get(), map.getValue(PREFIX_CLASS_NAME).get());
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
