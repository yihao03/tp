package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.classes.Class;

/**
 * Parses input arguments and creates a new AddClassCommand object
 */
public class AddClassCommandParser implements Parser<AddClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddClassCommand
     * and returns an AddClassCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddClassCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASSNAME, PREFIX_SUBJECT);

        if (!arePrefixesPresent(argMultimap, PREFIX_CLASSNAME, PREFIX_SUBJECT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLASSNAME, PREFIX_SUBJECT);
        String className = argMultimap.getValue(PREFIX_CLASSNAME).get();
        String subject = argMultimap.getValue(PREFIX_SUBJECT).get();

        // For now, create a class without a tutor (you may need to adjust this based on your requirements)
        // You might want to add a PREFIX_TUTOR or handle this differently
        Class classToAdd = new Class(className, subject);

        return new AddClassCommand(classToAdd);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
