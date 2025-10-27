package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListStudentsCommand;

public class ListStudentsCommandParserTest {

    private ListStudentsCommandParser parser = new ListStudentsCommandParser();

    @Test
    public void parse_validArgs_returnsListStudentsCommand() {
        assertParseSuccess(parser, " c/Math101", new ListStudentsCommand("Math101"));
    }

    @Test
    public void parse_validArgsWithSpaces_returnsListStudentsCommand() {
        assertParseSuccess(parser, " c/Advanced Math", new ListStudentsCommand("Advanced Math"));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, "Math101",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListStudentsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyClassName_throwsParseException() {
        assertParseFailure(parser, " c/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListStudentsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListStudentsCommand.MESSAGE_USAGE));
    }
}
