package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASSNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddClassCommand;
import seedu.address.model.classes.Class;

public class AddClassCommandParserTest {
    private final AddClassCommandParser parser = new AddClassCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Class expected = new Class("CS2103T", "SE");
        assertParseSuccess(parser, " " + PREFIX_CLASSNAME + "CS2103T " + PREFIX_SUBJECT + "SE",
                new AddClassCommand(expected));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassCommand.MESSAGE_USAGE);
        // missing class name
        assertParseFailure(parser, " " + PREFIX_SUBJECT + "SE", expectedMessage);
        // missing subject
        assertParseFailure(parser, " " + PREFIX_CLASSNAME + "CS2103T", expectedMessage);
        // missing both
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_preambleNotEmpty_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClassCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "preamble " + PREFIX_CLASSNAME + "CS2103T " + PREFIX_SUBJECT + "SE",
                expectedMessage);
    }
}
