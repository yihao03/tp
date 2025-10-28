package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.util.stream.Stream;

import seedu.address.logic.commands.ViewSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewSessionCommand object
 */
public class ViewSessionCommandParser implements Parser<ViewSessionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewSessionCommand
     * and returns a ViewSessionCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ViewSessionCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(trimmed, PREFIX_CLASS, PREFIX_SESSION);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASS, PREFIX_SESSION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewSessionCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASS, PREFIX_SESSION);

        String className = ParserUtil.parseClassName(argMultimap.getValue(PREFIX_CLASS).get());
        String sessionName = ParserUtil.parseSessionName(argMultimap.getValue(PREFIX_SESSION).get());

        return new ViewSessionCommand(className, sessionName);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
