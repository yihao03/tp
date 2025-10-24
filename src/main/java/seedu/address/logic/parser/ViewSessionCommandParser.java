package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.util.stream.Stream;

import seedu.address.logic.commands.ViewSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddClass object
 */
public class ViewSessionCommandParser implements Parser<ViewSessionCommand> {

    @Override
    public ViewSessionCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        String cmd = ViewSessionCommand.COMMAND_WORD;
        if (trimmed.regionMatches(true, 0, cmd, 0, cmd.length())) {
            trimmed = trimmed.substring(cmd.length()).trim();
        }

        ArgumentMultimap map = ArgumentTokenizer.tokenize(" " + trimmed, PREFIX_CLASS, PREFIX_SESSION);

        if (!arePrefixesPresent(map, PREFIX_CLASS, PREFIX_SESSION) || !map.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewSessionCommand.MESSAGE_USAGE));
        }

        String className = ParserUtil.parseClassName(map.getValue(PREFIX_CLASS).get());
        if (className.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewSessionCommand.MESSAGE_USAGE));
        }

        String sessionName = ParserUtil.parseClassName(map.getValue(PREFIX_SESSION).get());
        if (sessionName.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewSessionCommand.MESSAGE_USAGE));
        }

        return new ViewSessionCommand(className, sessionName);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
