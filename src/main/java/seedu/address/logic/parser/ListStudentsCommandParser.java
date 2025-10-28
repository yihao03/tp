package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;

import java.util.stream.Stream;

import seedu.address.logic.commands.ListStudentsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListStudentsCommand object.
 * This parser validates that the class name prefix is present and contains a valid value.
 */
public class ListStudentsCommandParser implements Parser<ListStudentsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListStudentsCommand
     * and returns a ListStudentsCommand object for execution.
     *
     * @param args the user input string containing the command arguments
     * @return a ListStudentsCommand object with the parsed class name
     * @throws ParseException if the user input does not conform to the expected format,
     *         contains duplicate prefixes, or has an empty class name
     */
    public ListStudentsCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLASS);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListStudentsCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASS);

        String className = argMultimap.getValue(PREFIX_CLASS).get().trim();

        if (className.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListStudentsCommand.MESSAGE_USAGE));
        }

        return new ListStudentsCommand(className);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     *
     * @param argumentMultimap the argument multimap containing parsed command arguments
     * @param prefixes the prefixes to check for presence
     * @return {@code true} if all specified prefixes are present and have values, {@code false} otherwise
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
