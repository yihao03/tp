package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.classroom.ClassName;
import seedu.address.model.classroom.TuitionClass;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;
import seedu.address.model.person.Tutor;
import seedu.address.model.tag.Tag;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_singlePrefix_success() {
        Prefix prefix = new Prefix("n/");
        String result = Messages.getErrorMessageForDuplicatePrefixes(prefix);
        assertTrue(result.contains("Multiple values specified"));
        assertTrue(result.contains("n/"));
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_multiplePrefixes_success() {
        Prefix prefix1 = new Prefix("n/");
        Prefix prefix2 = new Prefix("p/");
        String result = Messages.getErrorMessageForDuplicatePrefixes(prefix1, prefix2);
        assertTrue(result.contains("Multiple values specified"));
        assertTrue(result.contains("n/"));
        assertTrue(result.contains("p/"));
    }

    @Test
    public void format_person_success() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));
        Student student = new Student(
                new Name("John Doe"),
                new Phone("91234567"),
                new Email("john@example.com"),
                new Address("123 Main St"),
                tags
        );

        String result = Messages.format(student);
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("91234567"));
        assertTrue(result.contains("john@example.com"));
        assertTrue(result.contains("123 Main St"));
        assertTrue(result.contains("friend"));
    }

    @Test
    public void format_tuitionClassWithTutor_success() {
        Set<Tag> tags = new HashSet<>();
        Tutor tutor = new Tutor(
                new Name("Ms Lee"),
                new Phone("98765432"),
                new Email("lee@example.com"),
                new Address("456 School St"),
                tags
        );
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Math101"), tutor);

        String result = Messages.format(tuitionClass);
        assertTrue(result.contains("Math101"));
        assertTrue(result.contains("Ms Lee"));
    }

    @Test
    public void format_tuitionClassWithoutTutor_success() {
        TuitionClass tuitionClass = new TuitionClass(new ClassName("Physics201"));

        String result = Messages.format(tuitionClass);
        assertTrue(result.contains("Physics201"));
        assertTrue(result.contains("Unassigned"));
    }
}
