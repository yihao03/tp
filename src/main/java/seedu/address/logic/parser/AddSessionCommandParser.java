package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddSessionCommand object
 */
public class AddSessionCommandParser implements Parser<AddSessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSessionCommand
     * and returns an AddSessionCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddSessionCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(trimmed, PREFIX_CLASS, PREFIX_SESSION,
                PREFIX_DATETIME, PREFIX_LOCATION);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASS, PREFIX_SESSION, PREFIX_DATETIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddSessionCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASS, PREFIX_SESSION, PREFIX_DATETIME, PREFIX_LOCATION);

        String className = ParserUtil.parseClassName(argMultimap.getValue(PREFIX_CLASS).get());
        String sessionName = ParserUtil.parseSessionName(argMultimap.getValue(PREFIX_SESSION).get());

        // Check if datetime value contains extra text that might indicate wrong prefix usage
        String dateTimeStr = argMultimap.getValue(PREFIX_DATETIME).get();
        // If datetime has extra content beyond expected format, it likely means wrong prefix was used
        if (!dateTimeStr.trim().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            // Check if it looks like there's additional content that might be a mistyped prefix
            if (dateTimeStr.contains("/")) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        AddSessionCommand.MESSAGE_USAGE));
            }
        }

        LocalDateTime dateTime = ParserUtil.parseDateTime(dateTimeStr);
        String location = argMultimap.getValue(PREFIX_LOCATION).isPresent()
                ? ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION).get())
                : null;

        return new AddSessionCommand(className, sessionName, dateTime, location);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
