package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OLD_CLASS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditClassCommand;

public class EditClassCommandParserTest {

    private EditClassCommandParser parser = new EditClassCommandParser();

    @Test
    public void parse_validArgs_returnsEditClassCommand() {
        assertParseSuccess(parser, " o/Math101 c/Mathematics101",
                new EditClassCommand("Math101", "Mathematics101"));

        assertParseSuccess(parser, "   o/Math101   c/Mathematics101   ",
                new EditClassCommand("Math101", "Mathematics101"));

        assertParseSuccess(parser, " o/Advanced Math c/Advanced Mathematics",
                new EditClassCommand("Advanced Math", "Advanced Mathematics"));

        assertParseSuccess(parser, " c/Mathematics101 o/Math101",
                new EditClassCommand("Math101", "Mathematics101"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClassCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "Math101 c/Mathematics101",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClassCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "o/Math101 Mathematics101",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClassCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "o/Math101",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClassCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClassCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "extra o/Math101 c/Mathematics101",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClassCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertParseFailure(parser, " o/Math101 o/Math102 c/NewName",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OLD_CLASS));

        assertParseFailure(parser, " o/Math101 c/NewName c/AnotherName",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CLASS));
    }

    @Test
    public void parse_classNamesWithSpecialCharacters_successful() {
        assertParseSuccess(parser, " o/Sec2-Math-A c/Sec3-Math-A",
                new EditClassCommand("Sec2-Math-A", "Sec3-Math-A"));

        assertParseSuccess(parser, " o/CS_101 c/CS_201",
                new EditClassCommand("CS_101", "CS_201"));

        assertParseSuccess(parser, " o/Class1 c/Class2",
                new EditClassCommand("Class1", "Class2"));
    }
}
